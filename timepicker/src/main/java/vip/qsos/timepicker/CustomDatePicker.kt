package vip.qsos.timepicker

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.app.Dialog
import android.content.Context
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import kotlinx.android.synthetic.main.form_chose_time.*
import vip.qsos.form_n.R
import java.util.*

/**
 * @author 华清松
 *
 * 时间选择控件,构建时间选择窗口
 * @param startDate 最小可选时间
 * @param endDate   最大可选时间
 */
class CustomDatePicker constructor(
    private val context: Context,
    private val startDate: Date,
    private val endDate: Date,
    private val selectDate: Date,
    private val loop: Boolean = true,
    private val showYear: Boolean = true,
    private val showMonth: Boolean = true,
    private val showDay: Boolean = true,
    private val showHour: Boolean = true,
    private val showMinute: Boolean = true,
    private val onDateListener: OnDateListener
) {

    companion object {
        private const val MAX_MONTH = 12
        private const val MAX_HOUR = 23
        private const val MAX_MINUTE = 59

        private const val MIN_MINUTE = 0
        private const val MIN_HOUR = 0
    }

    enum class Type(val type: String) {
        YMDHM("yyyy-MM-dd HH:mm"),
        YMDH("yyyy-MM-dd HH"),
        YMD("yyyy-MM-dd"),
        YM("yyyy-MM"),
        Y("yyyy"),
        M("MM"),
        D("dd"),
        H("HH"),
        MDHM("MM-dd HH:mm"),
        MDH("MM-dd HH"),
        MD("MM-dd"),
        DH("dd HH"),
        DHM("dd HH:mm"),
        HM("HH:mm");
    }

    /**是否确认选择*/
    private var hasSelected = false

    private var startYear: Int = 0
    private var startMonth: Int = 0
    private var startDay: Int = 0
    private var startHour: Int = 0
    private var startMinute: Int = 0
    private var endYear: Int = 0
    private var endMonth: Int = 0
    private var endDay: Int = 0
    private var endHour: Int = 0
    private var endMinute: Int = 0

    private lateinit var dialog: Dialog

    private lateinit var tvCancel: TextView
    private lateinit var tvSure: TextView

    private lateinit var pYear: DatePickerView
    private lateinit var pMonth: DatePickerView
    private lateinit var pDay: DatePickerView
    private lateinit var pHour: DatePickerView
    private lateinit var pMinute: DatePickerView

    private lateinit var tYear: TextView
    private lateinit var tMonth: TextView
    private lateinit var tDay: TextView
    private lateinit var tHour: TextView
    private lateinit var tMinute: TextView

    private var listYear: ArrayList<String> = arrayListOf()
    private var listMonth: ArrayList<String> = arrayListOf()
    private var listDay: ArrayList<String> = arrayListOf()
    private var listHour: ArrayList<String> = arrayListOf()
    private var listMinute: ArrayList<String> = arrayListOf()

    private var selectedCalender = Calendar.getInstance()
    private var startCalendar = Calendar.getInstance()
    private var endCalendar = Calendar.getInstance()

    /**
     * @author : 华清松
     *
     * 配置选择器
     * @param type 时间选择格式，默认【年月日时分】
     * @param startDate 起始选择时间
     * @param endDate 结束选择时间
     * @param selectDate 已选择时间
     * @param onDateListener 选择结果回调监听
     */
    data class Builder(
        val context: Context, val type: Type = Type.YMDHM,
        val startDate: Date, val endDate: Date, val selectDate: Date,
        var loop: Boolean = true,
        val onDateListener: OnDateListener
    ) {

        var pass: Boolean = true

        init {
            this.pass = startDate <= endDate
        }

        fun build(): CustomDatePicker? {
            var showYear = true
            var showMonth = true
            var showDay = true
            var showHour = true
            var showMinute = true

            when (type) {
                Type.YMDHM -> {
                    showYear = true
                    showMonth = true
                    showDay = true
                    showHour = true
                    showMinute = true
                }
                Type.YMDH -> {
                    showYear = true
                    showMonth = true
                    showDay = true
                    showHour = true
                    showMinute = false
                }
                Type.YMD -> {
                    showYear = true
                    showMonth = true
                    showDay = true
                    showHour = false
                    showMinute = false
                }
                Type.YM -> {
                    showYear = true
                    showMonth = true
                    showDay = false
                    showHour = false
                    showMinute = false
                }
                Type.Y -> {
                    showYear = true
                    showMonth = false
                    showDay = false
                    showHour = false
                    showMinute = false
                }
                Type.MDHM -> {
                    showYear = false
                    showMonth = true
                    showDay = true
                    showHour = true
                    showMinute = true
                }
                Type.MDH -> {
                    showYear = false
                    showMonth = true
                    showDay = true
                    showHour = true
                    showMinute = false
                }
                Type.MD -> {
                    showYear = false
                    showMonth = true
                    showDay = true
                    showHour = false
                    showMinute = false
                }
                Type.DH -> {
                    showYear = false
                    showMonth = false
                    showDay = true
                    showHour = true
                    showMinute = false
                }
                Type.DHM -> {
                    showYear = false
                    showMonth = false
                    showDay = true
                    showHour = true
                    showMinute = true
                }
                Type.HM -> {
                    showYear = false
                    showMonth = false
                    showDay = false
                    showHour = true
                    showMinute = true
                }
                Type.M -> {
                    showYear = false
                    showMonth = false
                    showDay = false
                    showHour = false
                    showMinute = true
                }
                Type.H -> {
                    showYear = false
                    showMonth = false
                    showDay = false
                    showHour = true
                    showMinute = false
                }
                Type.D -> {
                    showYear = false
                    showMonth = false
                    showDay = true
                    showHour = false
                    showMinute = false
                }
            }
            return if (pass) {
                CustomDatePicker(
                    context = context,
                    startDate = startDate,
                    endDate = endDate,
                    selectDate = selectDate,
                    loop = loop,
                    showYear = showYear,
                    showMonth = showMonth,
                    showDay = showDay,
                    showHour = showHour,
                    showMinute = showMinute,
                    onDateListener = onDateListener
                )
            } else {
                null
            }

        }
    }

    /**弹出选择器*/
    fun show() {
        initDialog()
        initData()
        initSelected()
        dialog.show()
    }

    /**初始化弹窗*/
    private fun initDialog() {
        dialog = Dialog(context, R.style.FormRelativeTimeDialogStyle)
        dialog.setCancelable(true)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.form_chose_time)
        val window = dialog.window
        window!!.setGravity(Gravity.BOTTOM)
        val manager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val dm = DisplayMetrics()
        manager.defaultDisplay.getMetrics(dm)
        val lp = window.attributes
        lp.width = dm.widthPixels
        window.attributes = lp

        pYear = dialog.year_pv
        pMonth = dialog.month_pv
        pDay = dialog.day_pv
        pHour = dialog.hour_pv
        pMinute = dialog.minute_pv

        tvCancel = dialog.tv_cancel
        tvSure = dialog.tv_select

        tYear = dialog.timepicker_year_name
        tMonth = dialog.timepicker_month_name
        tDay = dialog.timepicker_day_name
        tHour = dialog.timepicker_hour_name
        tMinute = dialog.timepicker_minute_name

        pYear.setIsLoop(false)
        pMonth.setIsLoop(false)
        pDay.setIsLoop(loop)
        pHour.setIsLoop(loop)
        pMinute.setIsLoop(loop)

        tvCancel.setOnClickListener {
            hasSelected = false
            dialog.dismiss()
        }

        tvSure.setOnClickListener {
            hasSelected = true
            dialog.dismiss()
        }

        dialog.setOnDismissListener {
            if (hasSelected) {
                onDateListener.setDate(selectedCalender.time)
            } else {
                onDateListener.setDate(null)
            }
        }

        pYear.setOnSelectListener(object : DatePickerView.OnSelectListener {
            override fun onSelect(text: String) {
                selectedCalender.set(Calendar.YEAR, Integer.parseInt(text))
                changeMonth()
            }
        })

        pMonth.setOnSelectListener(object : DatePickerView.OnSelectListener {
            override fun onSelect(text: String) {
                selectedCalender.set(Calendar.DAY_OF_MONTH, 1)
                selectedCalender.set(Calendar.MONTH, Integer.parseInt(text) - 1)
                changeDay()
            }
        })

        pDay.setOnSelectListener(object : DatePickerView.OnSelectListener {
            override fun onSelect(text: String) {
                selectedCalender.set(Calendar.DAY_OF_MONTH, Integer.parseInt(text))
                changeHour()
            }
        })

        pHour.setOnSelectListener(object : DatePickerView.OnSelectListener {
            override fun onSelect(text: String) {
                selectedCalender.set(Calendar.HOUR_OF_DAY, Integer.parseInt(text))
                changeMinute()
            }
        })

        pMinute.setOnSelectListener(object : DatePickerView.OnSelectListener {
            override fun onSelect(text: String) {
                selectedCalender.set(Calendar.MINUTE, Integer.parseInt(text))
            }
        })
    }

    /**初始化数据*/
    private fun initData() {

        listYear.clear()
        listMonth.clear()
        listDay.clear()
        listHour.clear()
        listMinute.clear()

        startCalendar.time = startDate
        endCalendar.time = endDate

        startYear = startCalendar.get(Calendar.YEAR)
        startMonth = startCalendar.get(Calendar.MONTH) + 1
        startDay = startCalendar.get(Calendar.DAY_OF_MONTH)
        startHour = startCalendar.get(Calendar.HOUR_OF_DAY)
        startMinute = startCalendar.get(Calendar.MINUTE)

        endYear = endCalendar.get(Calendar.YEAR)
        endMonth = endCalendar.get(Calendar.MONTH) + 1
        endDay = endCalendar.get(Calendar.DAY_OF_MONTH)
        endHour = endCalendar.get(Calendar.HOUR_OF_DAY)
        endMinute = endCalendar.get(Calendar.MINUTE)

        initYearData()
        initMonthData()
        initDayData()
        initHourData()
        initMinuteData()

    }

    /**初始化已选时间*/
    private fun initSelected() {
        selectedCalender.time = selectDate

        val selectedYear = selectedCalender.get(Calendar.YEAR)
        val selectedMonth = selectedCalender.get(Calendar.MONTH) + 1
        val selectedDay = selectedCalender.get(Calendar.DAY_OF_MONTH)
        val selectedHour = selectedCalender.get(Calendar.HOUR_OF_DAY)
        val selectedMinute = selectedCalender.get(Calendar.MINUTE)

        tYear.visibility = if (showYear) View.VISIBLE else View.GONE
        pYear.visibility = if (showYear) View.VISIBLE else View.GONE
        pYear.setCanScroll(showYear && listYear.size > 1)
        pYear.setSelected(formatTimeUnit(selectedYear))

        tMonth.visibility = if (showMonth) View.VISIBLE else View.GONE
        pMonth.visibility = if (showMonth) View.VISIBLE else View.GONE
        pMonth.setCanScroll(showYear && listMonth.size > 1)
        pMonth.setSelected(formatTimeUnit(selectedMonth))

        tDay.visibility = if (showDay) View.VISIBLE else View.GONE
        pDay.visibility = if (showDay) View.VISIBLE else View.GONE
        pDay.setCanScroll(showDay && listDay.size > 1)
        pDay.setSelected(formatTimeUnit(selectedDay))

        tHour.visibility = if (showHour) View.VISIBLE else View.GONE
        pHour.visibility = if (showHour) View.VISIBLE else View.GONE
        pHour.setCanScroll(showHour && listHour.size > 1)
        pHour.setSelected(formatTimeUnit(selectedHour))

        tMinute.visibility = if (showMinute) View.VISIBLE else View.GONE
        pMinute.visibility = if (showMinute) View.VISIBLE else View.GONE
        pMinute.setCanScroll(showMinute && listMinute.size > 1)
        pMinute.setSelected(formatTimeUnit(selectedMinute))

    }

    /**初始化年数据*/
    private fun initYearData() {
        listYear.clear()
        for (i in startYear..endYear) {
            listYear.add(formatTimeUnit(i))
        }
        pYear.setData(listYear)
    }

    /**初始化月份数据*/
    private fun initMonthData() {
        listMonth.clear()
        when (selectedCalender.get(Calendar.YEAR)) {
            startYear -> for (i in startMonth..MAX_MONTH) {
                listMonth.add(formatTimeUnit(i))
            }
            endYear -> for (i in 1..endMonth) {
                listMonth.add(formatTimeUnit(i))
            }
            else -> for (i in 1..MAX_MONTH) {
                listMonth.add(formatTimeUnit(i))
            }
        }
        pMonth.setData(listMonth)
    }

    /**修改月份数据*/
    private fun changeMonth() {
        initMonthData()
        pMonth.setSelected(0)
        selectedCalender.set(Calendar.MONTH, Integer.parseInt(listMonth[0]) - 1)
        executeAnimator(pMonth)
        pMonth.postDelayed({ changeDay() }, 100)
    }

    /**初始化日数据*/
    private fun initDayData() {
        listDay.clear()
        val selectedYear = selectedCalender.get(Calendar.YEAR)
        val selectedMonth = selectedCalender.get(Calendar.MONTH) + 1
        if (selectedYear == startYear && selectedMonth == startMonth) {
            for (i in startDay..selectedCalender.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                listDay.add(formatTimeUnit(i))
            }
        } else if (selectedYear == endYear && selectedMonth == endMonth) {
            for (i in 1..endDay) {
                listDay.add(formatTimeUnit(i))
            }
        } else {
            for (i in 1..selectedCalender.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                listDay.add(formatTimeUnit(i))
            }
        }
        pDay.setData(listDay)
    }

    /**修改日数据*/
    private fun changeDay() {
        initDayData()
        selectedCalender.set(Calendar.DAY_OF_MONTH, Integer.parseInt(listDay[0]))
        pDay.setSelected(0)
        executeAnimator(pDay)
        pDay.postDelayed({ changeHour() }, 100)
    }

    /**初始化小时数据*/
    private fun initHourData() {
        listHour.clear()
        val selectedYear = selectedCalender.get(Calendar.YEAR)
        val selectedMonth = selectedCalender.get(Calendar.MONTH) + 1
        val selectedDay = selectedCalender.get(Calendar.DAY_OF_MONTH)
        if (selectedYear == startYear && selectedMonth == startMonth && selectedDay == startDay) {
            for (i in startHour..MAX_HOUR) {
                listHour.add(formatTimeUnit(i))
            }
        } else if (selectedYear == endYear && selectedMonth == endMonth && selectedDay == endDay) {
            for (i in MIN_HOUR..endHour) {
                listHour.add(formatTimeUnit(i))
            }
        } else {
            for (i in MIN_HOUR..MAX_HOUR) {
                listHour.add(formatTimeUnit(i))
            }
        }
        pHour.setData(listHour)
    }

    /**修改小时数据*/
    private fun changeHour() {
        initHourData()
        selectedCalender.set(Calendar.HOUR_OF_DAY, Integer.parseInt(listHour[0]))
        pHour.setSelected(0)
        executeAnimator(pHour)

        pHour.postDelayed({ changeMinute() }, 100)
    }

    /**初始化分钟数据*/
    private fun initMinuteData() {
        listMinute.clear()
        val selectedYear = selectedCalender.get(Calendar.YEAR)
        val selectedMonth = selectedCalender.get(Calendar.MONTH) + 1
        val selectedDay = selectedCalender.get(Calendar.DAY_OF_MONTH)
        val selectedHour = selectedCalender.get(Calendar.HOUR_OF_DAY)
        if (selectedYear == startYear && selectedMonth == startMonth && selectedDay == startDay && selectedHour == startHour) {
            for (i in startMinute..MAX_MINUTE) {
                listMinute.add(formatTimeUnit(i))
            }
        } else if (selectedYear == endYear && selectedMonth == endMonth && selectedDay == endDay && selectedHour == endHour) {
            for (i in MIN_MINUTE..endMinute) {
                listMinute.add(formatTimeUnit(i))
            }
        } else {
            for (i in MIN_MINUTE..MAX_MINUTE) {
                listMinute.add(formatTimeUnit(i))
            }
        }
        pMinute.setData(listMinute)
    }

    /**修改分钟数据*/
    private fun changeMinute() {
        if (showMinute) {
            initMinuteData()
            selectedCalender.set(Calendar.MINUTE, Integer.parseInt(listMinute[0]))
            pMinute.setSelected(0)
            executeAnimator(pMinute)
        }
    }

    /**文字动画*/
    private fun executeAnimator(view: View?) {
        val pvhX = PropertyValuesHolder.ofFloat("alpha", 1f, 0f, 1f)
        val pvhY = PropertyValuesHolder.ofFloat("scaleX", 1f, 1.3f, 1f)
        val pvhZ = PropertyValuesHolder.ofFloat("scaleY", 1f, 1.3f, 1f)
        ObjectAnimator.ofPropertyValuesHolder(view, pvhX, pvhY, pvhZ).setDuration(100).start()
    }

    /**将“0-9”转换为“00-09”*/
    private fun formatTimeUnit(unit: Int): String {
        return if (unit < 10) "0$unit" else unit.toString()
    }

}
