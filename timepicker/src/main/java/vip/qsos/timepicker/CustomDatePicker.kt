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
import vip.qsos.form_n.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author 华清松
 * @doc 类说明：时间选择控件,构建时间选择窗口
 * @param startDate 最小可选时间
 * @param endDate   最大可选时间
 */
class CustomDatePicker(
        context: Context,
        private val sdf: SimpleDateFormat,
        startDate: String,
        endDate: String,
        resultHandler: OnDateListener
) {

    private var selectState = false

    private var scrollUnits = SCROLL_TYPE.HOUR.value + SCROLL_TYPE.MINUTE.value
    private var canAccess = false
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
    private var spanYear: Boolean = false
    private var spanMon: Boolean = false
    private var spanDay: Boolean = false
    private var spanHour: Boolean = false
    private var spanMin: Boolean = false
    private var isStartTime = true

    private var handler: OnDateListener? = null
    private var context: Context? = null
    var dialog: Dialog? = null
        private set

    private var yearPv: DatePickerView? = null
    private var monthPv: DatePickerView? = null
    private var dayPv: DatePickerView? = null
    private var hourPv: DatePickerView? = null
    private var minutePv: DatePickerView? = null

    private var year: ArrayList<String>? = null
    private var month: ArrayList<String>? = null
    private var day: ArrayList<String>? = null
    private var hour: ArrayList<String>? = null
    private var minute: ArrayList<String>? = null
    private var selectedCalender = Calendar.getInstance()
    private var startCalendar = Calendar.getInstance()
    private var endCalendar = Calendar.getInstance()
    private var tv_cancel: TextView? = null
    private var tv_select: TextView? = null
    private var day_tv: TextView? = null
    private var hour_text: TextView? = null
    private var minute_text: TextView? = null
    private var secondStartCalender = Calendar.getInstance()
    private var secondEndCalender = Calendar.getInstance()

    private enum class SCROLL_TYPE constructor(var value: Int) {
        HOUR(1),
        MINUTE(2)
    }

    init {
        if (isValidDate(startDate) && isValidDate(endDate)) {
            canAccess = true
            this.context = context
            this.handler = resultHandler
            try {
                startCalendar!!.time = sdf.parse(startDate)
                endCalendar!!.time = sdf.parse(endDate)
                secondStartCalender!!.time = sdf.parse(startDate)
                secondEndCalender!!.time = sdf.parse(endDate)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            initDialog()
            initView()
        }
    }

    private fun initDialog() {
        if (dialog == null) {
            dialog = Dialog(context!!, R.style.FormRelativeTimeDialogStyle)
            dialog!!.setCancelable(true)
            dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog!!.setContentView(R.layout.form_chose_time)
            val window = dialog!!.window
            window!!.setGravity(Gravity.BOTTOM)
            val manager = context!!.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val dm = DisplayMetrics()
            manager.defaultDisplay.getMetrics(dm)
            val lp = window.attributes
            lp.width = dm.widthPixels
            window.attributes = lp
        }
    }

    private fun initView() {
        yearPv = dialog!!.findViewById(R.id.year_pv)
        monthPv = dialog!!.findViewById(R.id.month_pv)
        dayPv = dialog!!.findViewById(R.id.day_pv)
        day_tv = dialog!!.findViewById(R.id.day_tv)
        hourPv = dialog!!.findViewById(R.id.hour_pv)
        minutePv = dialog!!.findViewById(R.id.minute_pv)
        tv_cancel = dialog!!.findViewById(R.id.tv_cancel)
        tv_select = dialog!!.findViewById(R.id.tv_select)
        hour_text = dialog!!.findViewById(R.id.hour_text)
        minute_text = dialog!!.findViewById(R.id.minute_text)

        tv_cancel!!.setOnClickListener {
            selectState = false
            dialog!!.dismiss()
        }

        tv_select!!.setOnClickListener {
            selectState = true
            dialog!!.dismiss()
        }

        dialog!!.setOnDismissListener {
            if (selectState) {
                handler!!.setDate(1, selectedCalender.time)
            } else {
                handler!!.setDate(-1, null)
            }
        }
    }

    private fun initParameter(calendar1: Calendar = startCalendar, calendar2: Calendar = endCalendar) {

        startYear = calendar1.get(Calendar.YEAR)
        startMonth = calendar1.get(Calendar.MONTH) + 1
        startDay = calendar1.get(Calendar.DAY_OF_MONTH)
        startHour = calendar1.get(Calendar.HOUR_OF_DAY)
        startMinute = calendar1.get(Calendar.MINUTE)
        endYear = calendar2.get(Calendar.YEAR)
        endMonth = calendar2.get(Calendar.MONTH) + 1
        endDay = calendar2.get(Calendar.DAY_OF_MONTH)
        endHour = calendar2.get(Calendar.HOUR_OF_DAY)
        endMinute = calendar2.get(Calendar.MINUTE)

        spanYear = startYear != endYear
        spanMon = !spanYear && startMonth != endMonth
        spanDay = !spanMon && startDay != endDay
        spanHour = !spanDay && startHour != endHour
        spanMin = !spanHour && startMinute != endMinute

        selectedCalender!!.time = calendar1.time
    }

    private fun initTimer(calendar: Calendar? = startCalendar) {
        initArrayList()
        if (spanYear) {
            for (i in startYear..endYear) {
                year!!.add(i.toString())
            }
            for (i in startMonth..MAX_MONTH) {
                month!!.add(formatTimeUnit(i))
            }
            for (i in startDay..calendar!!.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                day!!.add(formatTimeUnit(i))
            }

            if (scrollUnits and SCROLL_TYPE.HOUR.value != SCROLL_TYPE.HOUR.value) {
                hour!!.add(formatTimeUnit(startHour))
            } else {
                for (i in startHour..MAX_HOUR) {
                    hour!!.add(formatTimeUnit(i))
                }
            }

            if (scrollUnits and SCROLL_TYPE.MINUTE.value != SCROLL_TYPE.MINUTE.value) {
                minute!!.add(formatTimeUnit(startMinute))
            } else {
                for (i in startMinute..MAX_MINUTE) {
                    minute!!.add(formatTimeUnit(i))
                }
            }
        } else if (spanMon) {
            year!!.add(startYear.toString())
            for (i in startMonth..endMonth) {
                month!!.add(formatTimeUnit(i))
            }
            for (i in startDay..calendar!!.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                day!!.add(formatTimeUnit(i))
            }

            if (scrollUnits and SCROLL_TYPE.HOUR.value != SCROLL_TYPE.HOUR.value) {
                hour!!.add(formatTimeUnit(startHour))
            } else {
                for (i in startHour..MAX_HOUR) {
                    hour!!.add(formatTimeUnit(i))
                }
            }

            if (scrollUnits and SCROLL_TYPE.MINUTE.value != SCROLL_TYPE.MINUTE.value) {
                minute!!.add(formatTimeUnit(startMinute))
            } else {
                for (i in startMinute..MAX_MINUTE) {
                    minute!!.add(formatTimeUnit(i))
                }
            }
        } else if (spanDay) {
            year!!.add(startYear.toString())
            month!!.add(formatTimeUnit(startMonth))
            for (i in startDay..endDay) {
                day!!.add(formatTimeUnit(i))
            }

            if (scrollUnits and SCROLL_TYPE.HOUR.value != SCROLL_TYPE.HOUR.value) {
                hour!!.add(formatTimeUnit(startHour))
            } else {
                for (i in startHour..MAX_HOUR) {
                    hour!!.add(formatTimeUnit(i))
                }
            }

            if (scrollUnits and SCROLL_TYPE.MINUTE.value != SCROLL_TYPE.MINUTE.value) {
                minute!!.add(formatTimeUnit(startMinute))
            } else {
                for (i in startMinute..MAX_MINUTE) {
                    minute!!.add(formatTimeUnit(i))
                }
            }
        } else if (spanHour) {
            year!!.add(startYear.toString())
            month!!.add(formatTimeUnit(startMonth))
            day!!.add(formatTimeUnit(startDay))
            if (scrollUnits and SCROLL_TYPE.HOUR.value != SCROLL_TYPE.HOUR.value) {
                hour!!.add(formatTimeUnit(startHour))
            } else {
                for (i in startHour..endHour) {
                    hour!!.add(formatTimeUnit(i))
                }
            }

            if (scrollUnits and SCROLL_TYPE.MINUTE.value != SCROLL_TYPE.MINUTE.value) {
                minute!!.add(formatTimeUnit(startMinute))
            } else {
                for (i in startMinute..MAX_MINUTE) {
                    minute!!.add(formatTimeUnit(i))
                }
            }
        } else if (spanMin) {
            year!!.add(startYear.toString())
            month!!.add(formatTimeUnit(startMonth))
            day!!.add(formatTimeUnit(startDay))
            hour!!.add(formatTimeUnit(startHour))

            if (scrollUnits and SCROLL_TYPE.MINUTE.value != SCROLL_TYPE.MINUTE.value) {
                minute!!.add(formatTimeUnit(startMinute))
            } else {
                for (i in startMinute..endMinute) {
                    minute!!.add(formatTimeUnit(i))
                }
            }
        }
        loadComponent()
    }

    /**
     * 将“0-9”转换为“00-09”
     */
    private fun formatTimeUnit(unit: Int): String {
        return if (unit < 10) "0$unit" else unit.toString()
    }

    private fun initArrayList() {
        if (year == null) {
            year = ArrayList()
        }
        if (month == null) {
            month = ArrayList()
        }
        if (day == null) {
            day = ArrayList()
        }
        if (hour == null) {
            hour = ArrayList()
        }
        if (minute == null) {
            minute = ArrayList()
        }
        year!!.clear()
        month!!.clear()
        day!!.clear()
        hour!!.clear()
        minute!!.clear()
    }

    private fun loadComponent() {
        yearPv!!.setData(year!!)
        monthPv!!.setData(month!!)
        dayPv!!.setData(day!!)
        hourPv!!.setData(hour!!)
        minutePv!!.setData(minute!!)
        yearPv!!.setSelected(0)
        monthPv!!.setSelected(0)
        dayPv!!.setSelected(0)
        hourPv!!.setSelected(0)
        minutePv!!.setSelected(0)
        executeScroll()
    }

    private fun addListener() {
        yearPv!!.setOnSelectListener(object : DatePickerView.OnSelectListener {
            override fun onSelect(text: String) {
                selectedCalender!!.set(Calendar.YEAR, Integer.parseInt(text))
                monthChange()
            }
        })

        monthPv!!.setOnSelectListener(object : DatePickerView.OnSelectListener {
            override fun onSelect(text: String) {
                selectedCalender!!.set(Calendar.DAY_OF_MONTH, 1)
                selectedCalender!!.set(Calendar.MONTH, Integer.parseInt(text) - 1)
                dayChange()
            }
        })

        dayPv!!.setOnSelectListener(
                object : DatePickerView.OnSelectListener {
                    override fun onSelect(text: String) {
                        selectedCalender!!.set(Calendar.DAY_OF_MONTH, Integer.parseInt(text))
                        hourChange()
                    }
                })

        hourPv!!.setOnSelectListener(
                object : DatePickerView.OnSelectListener {
                    override fun onSelect(text: String) {
                        selectedCalender!!.set(Calendar.HOUR_OF_DAY, Integer.parseInt(text))
                        minuteChange()
                    }
                })

        minutePv!!.setOnSelectListener(
                object : DatePickerView.OnSelectListener {
                    override fun onSelect(text: String) {
                        selectedCalender!!.set(Calendar.MINUTE, Integer.parseInt(text))
                    }
                })
    }

    private fun monthChange() {
        month!!.clear()
        when (selectedCalender!!.get(Calendar.YEAR)) {
            startYear -> for (i in startMonth..MAX_MONTH) {
                month!!.add(formatTimeUnit(i))
            }
            endYear -> for (i in 1..endMonth) {
                month!!.add(formatTimeUnit(i))
            }
            else -> for (i in 1..MAX_MONTH) {
                month!!.add(formatTimeUnit(i))
            }
        }
        selectedCalender!!.set(Calendar.MONTH, Integer.parseInt(month!![0]) - 1)
        monthPv!!.setData(month!!)
        monthPv!!.setSelected(0)
        executeAnimator(monthPv)

        monthPv!!.postDelayed({ dayChange() }, 100)
    }

    private fun dayChange() {
        day!!.clear()
        val selectedYear = selectedCalender!!.get(Calendar.YEAR)
        val selectedMonth = selectedCalender!!.get(Calendar.MONTH) + 1
        if (selectedYear == startYear && selectedMonth == startMonth) {
            for (i in startDay..selectedCalender!!.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                day!!.add(formatTimeUnit(i))
            }
        } else if (selectedYear == endYear && selectedMonth == endMonth) {
            for (i in 1..endDay) {
                day!!.add(formatTimeUnit(i))
            }
        } else {
            for (i in 1..selectedCalender!!.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                day!!.add(formatTimeUnit(i))
            }
        }
        selectedCalender!!.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day!![0]))
        dayPv!!.setData(day!!)
        dayPv!!.setSelected(0)
        executeAnimator(dayPv)

        dayPv!!.postDelayed({ hourChange() }, 100)
    }

    private fun hourChange() {
        if (scrollUnits and SCROLL_TYPE.HOUR.value == SCROLL_TYPE.HOUR.value) {
            hour!!.clear()
            val selectedYear = selectedCalender!!.get(Calendar.YEAR)
            val selectedMonth = selectedCalender!!.get(Calendar.MONTH) + 1
            val selectedDay = selectedCalender!!.get(Calendar.DAY_OF_MONTH)
            if (selectedYear == startYear && selectedMonth == startMonth && selectedDay == startDay) {
                for (i in startHour..MAX_HOUR) {
                    hour!!.add(formatTimeUnit(i))
                }
            } else if (selectedYear == endYear && selectedMonth == endMonth && selectedDay == endDay) {
                for (i in MIN_HOUR..endHour) {
                    hour!!.add(formatTimeUnit(i))
                }
            } else {
                for (i in MIN_HOUR..MAX_HOUR) {
                    hour!!.add(formatTimeUnit(i))
                }
            }
            selectedCalender!!.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour!![0]))
            hourPv!!.setData(hour!!)
            hourPv!!.setSelected(0)
            executeAnimator(hourPv)
        }

        hourPv!!.postDelayed({ minuteChange() }, 100)
    }

    private fun minuteChange() {
        if (scrollUnits and SCROLL_TYPE.MINUTE.value == SCROLL_TYPE.MINUTE.value) {
            minute!!.clear()
            val selectedYear = selectedCalender!!.get(Calendar.YEAR)
            val selectedMonth = selectedCalender!!.get(Calendar.MONTH) + 1
            val selectedDay = selectedCalender!!.get(Calendar.DAY_OF_MONTH)
            val selectedHour = selectedCalender!!.get(Calendar.HOUR_OF_DAY)
            if (selectedYear == startYear && selectedMonth == startMonth && selectedDay == startDay && selectedHour == startHour) {
                for (i in startMinute..MAX_MINUTE) {
                    minute!!.add(formatTimeUnit(i))
                }
            } else if (selectedYear == endYear && selectedMonth == endMonth && selectedDay == endDay && selectedHour == endHour) {
                for (i in MIN_MINUTE..endMinute) {
                    minute!!.add(formatTimeUnit(i))
                }
            } else {
                for (i in MIN_MINUTE..MAX_MINUTE) {
                    minute!!.add(formatTimeUnit(i))
                }
            }
            selectedCalender!!.set(Calendar.MINUTE, Integer.parseInt(minute!![0]))
            minutePv!!.setData(minute!!)
            minutePv!!.setSelected(0)
            executeAnimator(minutePv)
        }
        executeScroll()
    }

    private fun executeAnimator(view: View?) {
        val pvhX = PropertyValuesHolder.ofFloat("alpha", 1f, 0f, 1f)
        val pvhY = PropertyValuesHolder.ofFloat("scaleX", 1f, 1.3f, 1f)
        val pvhZ = PropertyValuesHolder.ofFloat("scaleY", 1f, 1.3f, 1f)
        ObjectAnimator.ofPropertyValuesHolder(view, pvhX, pvhY, pvhZ).setDuration(200).start()
    }

    private fun executeScroll() {
        yearPv!!.setCanScroll(year!!.size > 1)
        monthPv!!.setCanScroll(month!!.size > 1)
        dayPv!!.setCanScroll(day!!.size > 1)
        hourPv!!.setCanScroll(hour!!.size > 1 && scrollUnits and SCROLL_TYPE.HOUR.value == SCROLL_TYPE.HOUR.value)
        minutePv!!.setCanScroll(minute!!.size > 1 && scrollUnits and SCROLL_TYPE.MINUTE.value == SCROLL_TYPE.MINUTE.value)
    }

    private fun disScrollUnit(vararg scroll_types: SCROLL_TYPE): Int {
        if (scroll_types.isEmpty()) {
            scrollUnits = SCROLL_TYPE.HOUR.value + SCROLL_TYPE.MINUTE.value
        } else {
            for (scroll_type in scroll_types) {
                scrollUnits = scrollUnits xor scroll_type.value
            }
        }
        return scrollUnits
    }

    fun show(time: String) {
        if (canAccess) {
            if (isValidDate(time)) {
                /*判断时间*/
                if (startCalendar!!.time.time < endCalendar!!.time.time) {
                    canAccess = true
                    if (isStartTime) {
                        initParameter()
                    } else {
                        initParameter(secondStartCalender!!, secondEndCalender!!)
                    }
                    initTimer()
                    addListener()
                    setSelectedTime(time)
                    dialog!!.show()
                }
            } else {
                canAccess = false
            }
        }
    }

    /**
     * 设置日期控件是否显示时和分
     */
    private fun showSpecificTime(show: Boolean) {
        if (canAccess) {
            if (show) {
                disScrollUnit()
                hourPv!!.visibility = View.VISIBLE
                hour_text!!.visibility = View.VISIBLE
                minutePv!!.visibility = View.VISIBLE
                minute_text!!.visibility = View.VISIBLE
            } else {
                disScrollUnit(
                    SCROLL_TYPE.HOUR,
                    SCROLL_TYPE.MINUTE
                )
                hourPv!!.visibility = View.GONE
                hour_text!!.visibility = View.GONE
                minutePv!!.visibility = View.GONE
                minute_text!!.visibility = View.GONE
            }
        }
    }

    /**
     * 设置日期控件是否显示日
     */
    fun showDayTime(showDay: Boolean, showSpecific: Boolean) {
        if (canAccess) {
            if (showDay) {
                disScrollUnit()
                dayPv!!.visibility = View.VISIBLE
                day_tv!!.visibility = View.VISIBLE
                showSpecificTime(showSpecific)
            } else {
                disScrollUnit(
                    SCROLL_TYPE.HOUR,
                    SCROLL_TYPE.MINUTE
                )
                dayPv!!.visibility = View.GONE
                day_tv!!.visibility = View.GONE
                showSpecificTime(false)
            }
        }
    }

    /**
     * 设置日期控件是否可以循环滚动
     */
    fun setIsLoop(isLoop: Boolean) {
        if (canAccess) {
            this.yearPv!!.setIsLoop(isLoop)
            this.monthPv!!.setIsLoop(isLoop)
            this.dayPv!!.setIsLoop(isLoop)
            this.hourPv!!.setIsLoop(isLoop)
            this.minutePv!!.setIsLoop(isLoop)
        }
    }

    /**
     * 设置日期控件默认选中的时间
     */
    private fun setSelectedTime(time: String) {
        if (canAccess) {
            val str = time.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val dateStr = str[0].split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            yearPv!!.setSelected(dateStr[0])
            selectedCalender!!.set(Calendar.YEAR, Integer.parseInt(dateStr[0]))

            month!!.clear()
            val selectedYear = selectedCalender!!.get(Calendar.YEAR)
            if (startYear == endYear) {
                for (i in startMonth..endMonth) {
                    month!!.add(formatTimeUnit(i))
                }
            } else {
                when (selectedYear) {
                    startYear -> for (i in startMonth..MAX_MONTH) {
                        month!!.add(formatTimeUnit(i))
                    }
                    endYear -> for (i in 1..endMonth) {
                        month!!.add(formatTimeUnit(i))
                    }
                    else -> for (i in 1..MAX_MONTH) {
                        month!!.add(formatTimeUnit(i))
                    }
                }
            }
            monthPv!!.setData(month!!)
            monthPv!!.setSelected(dateStr[1])
            selectedCalender!!.set(Calendar.MONTH, Integer.parseInt(dateStr[1]) - 1)
            executeAnimator(monthPv)

            day!!.clear()
            val selectedMonth = selectedCalender!!.get(Calendar.MONTH) + 1
            if (startYear == endYear && startMonth == endMonth) {
                for (i in startDay..endDay) {
                    day!!.add(formatTimeUnit(i))
                }
            } else {
                if (selectedYear == startYear && selectedMonth == startMonth) {
                    for (i in startDay..selectedCalender!!.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                        day!!.add(formatTimeUnit(i))
                    }
                } else if (selectedYear == endYear && selectedMonth == endMonth) {
                    for (i in 1..endDay) {
                        day!!.add(formatTimeUnit(i))
                    }
                } else {
                    for (i in 1..selectedCalender!!.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                        day!!.add(formatTimeUnit(i))
                    }
                }
            }
            dayPv!!.setData(day!!)
            dayPv!!.setSelected(dateStr[2])
            selectedCalender!!.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateStr[2]))
            executeAnimator(dayPv)

            if (str.size == 2) {
                val timeStr = str[1].split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

                if (scrollUnits and SCROLL_TYPE.HOUR.value == SCROLL_TYPE.HOUR.value) {
                    hour!!.clear()
                    val selectedDay = selectedCalender!!.get(Calendar.DAY_OF_MONTH)
                    if (selectedYear == startYear && selectedMonth == startMonth && selectedDay == startDay) {
                        for (i in startHour..MAX_HOUR) {
                            hour!!.add(formatTimeUnit(i))
                        }
                    } else if (selectedYear == endYear && selectedMonth == endMonth && selectedDay == endDay) {
                        for (i in MIN_HOUR..endHour) {
                            hour!!.add(formatTimeUnit(i))
                        }
                    } else {
                        for (i in MIN_HOUR..MAX_HOUR) {
                            hour!!.add(formatTimeUnit(i))
                        }
                    }
                    hourPv!!.setData(hour!!)
                    hourPv!!.setSelected(timeStr[0])
                    selectedCalender!!.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeStr[0]))
                    executeAnimator(hourPv)
                }

                if (scrollUnits and SCROLL_TYPE.MINUTE.value == SCROLL_TYPE.MINUTE.value) {
                    minute!!.clear()
                    val selectedDay = selectedCalender!!.get(Calendar.DAY_OF_MONTH)
                    val selectedHour = selectedCalender!!.get(Calendar.HOUR_OF_DAY)
                    if (selectedYear == startYear && selectedMonth == startMonth && selectedDay == startDay && selectedHour == startHour) {
                        for (i in startMinute..MAX_MINUTE) {
                            minute!!.add(formatTimeUnit(i))
                        }
                    } else if (selectedYear == endYear && selectedMonth == endMonth && selectedDay == endDay && selectedHour == endHour) {
                        for (i in MIN_MINUTE..endMinute) {
                            minute!!.add(formatTimeUnit(i))
                        }
                    } else {
                        for (i in MIN_MINUTE..MAX_MINUTE) {
                            minute!!.add(formatTimeUnit(i))
                        }
                    }
                    minutePv!!.setData(minute!!)
                    minutePv!!.setSelected(timeStr[1])
                    selectedCalender!!.set(Calendar.MINUTE, Integer.parseInt(timeStr[1]))
                    executeAnimator(minutePv)
                }
            }
            executeScroll()
        }
    }

    /**
     * 验证字符串是否是一个合法的日期格式
     */
    private fun isValidDate(date: String): Boolean {
        var convertSuccess = true
        // 指定日期格式
        try {
            // 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2015/02/29会被接受，并转换成2015/03/01
            sdf.isLenient = false
            sdf.parse(date)
        } catch (e: Exception) {
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            convertSuccess = false
        }

        return convertSuccess
    }

    companion object {
        private const val MAX_MINUTE = 59
        private const val MAX_HOUR = 23
        private const val MIN_MINUTE = 0
        private const val MIN_HOUR = 0
        private const val MAX_MONTH = 12
    }
}
