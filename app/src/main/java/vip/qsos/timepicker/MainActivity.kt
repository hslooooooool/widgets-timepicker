package vip.qsos.timepicker

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.android.synthetic.main.main_activity.*
import java.util.*


/**
 * @author : 华清松
 */
class MainActivity : AppCompatActivity() {

    private lateinit var mTimePickerAdapter: TimePickerAdapter
    private lateinit var mLayoutManager: RecyclerView.LayoutManager
    private lateinit var mContext: Context
    private val list = arrayListOf(
        "普通时间\n格式：年月日时分\n限制：当前时间上下【10年】",

        "定制时间\n格式：年月日时\n限制：当前时间上下【10年】",
        "定制时间\n格式：年月日\n限制：当前时间上下【10年】",
        "定制时间\n格式：年月\n限制：当前时间上下【10年】",
        "定制时间\n格式：年\n限制：当前时间上下【10年】",

        "定制时间\n格式：【今月】日时分\n限制：当前时间【今月】",
        "定制时间\n格式：【今月】日时\n限制：当前时间【今月】",
        "定制时间\n格式：【今日】时分\n限制：当前时间【今日】",

        "限制时间\n格式：年月日时分\n限制：设定时间上下【1年】",
        "限制时间\n格式：年月日时分\n限制：当前时间上下【1年】",
        "限制时间\n格式：年月日时分\n限制：设定时间上下【5年】",
        "限制时间\n格式：年月日时分\n限制：当前时间上下【5年】",

        "限制时间\n格式：年月日时分\n限制：设定时间往前【5年】",
        "限制时间\n格式：年月日时分\n限制：当前时间往前【5年】",
        "限制时间\n格式：年月日时分\n限制：设定时间往后【5年】",
        "限制时间\n格式：年月日时分\n限制：当前时间往后【5年】",

        "限制时间\n格式：年月日时分\n限制：设定时间往前【5年】，往后【1年】",
        "限制时间\n格式：年月日时分\n限制：当前时间往前【5年】，往后【1年】",
        "错误时间\n格式：年月日时分\n错误：开始时间大于设定时间或结束时间小于设定时间"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        mContext = this

        mLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        timepicker_list.layoutManager = mLayoutManager

        mTimePickerAdapter = TimePickerAdapter(list)
        timepicker_list.adapter = mTimePickerAdapter
        mTimePickerAdapter.setOnItemClickListener(object : TimePickerAdapter.OnItemClickListener {
            override fun onClick(p: Int, data: String) {
                val nowDate = Date()
                val mCalendar = Calendar.getInstance()
                mCalendar.time = nowDate

                val nowYear = mCalendar.get(Calendar.YEAR)
                val nowMonth = mCalendar.get(Calendar.MONTH)
                val nowDay = mCalendar.get(Calendar.DAY_OF_MONTH)

                when (p) {
                    0 -> {
                        TimePickHelper.picker(
                            context = mContext, timeType = CustomDatePicker.Type.YMDHM,
                            onDateListener = onDateListener
                        )
                    }
                    1 -> {
                        TimePickHelper.picker(
                            context = mContext, timeType = CustomDatePicker.Type.YMDH,
                            onDateListener = onDateListener
                        )
                    }
                    2 -> {
                        TimePickHelper.picker(
                            context = mContext, timeType = CustomDatePicker.Type.YMD,
                            onDateListener = onDateListener
                        )
                    }
                    3 -> {
                        TimePickHelper.picker(
                            context = mContext, timeType = CustomDatePicker.Type.YM,
                            onDateListener = onDateListener
                        )
                    }
                    4 -> {
                        TimePickHelper.picker(
                            context = mContext, timeType = CustomDatePicker.Type.Y,
                            onDateListener = onDateListener
                        )
                    }
                    5 -> {
                        mCalendar.set(nowYear, nowMonth, 0, 0, 0)
                        val limitDateStart = mCalendar.time

                        val mMaxDay = mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)
                        mCalendar.set(nowYear, nowMonth, mMaxDay, 23, 59)
                        val limitDateEnd = mCalendar.time
                        TimePickHelper.picker(
                            context = mContext,
                            timeType = CustomDatePicker.Type.DHM,
                            limitMin = limitDateStart,
                            limitMax = limitDateEnd,
                            selected = nowDate,
                            onDateListener = onDateListener
                        )
                    }
                    6 -> {
                        mCalendar.set(nowYear, nowMonth, 0, 0, 0)
                        val limitDateStart = mCalendar.time

                        val mMaxDay = mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)
                        val mMaxHour = mCalendar.getActualMaximum(Calendar.HOUR_OF_DAY)
                        mCalendar.set(nowYear, nowMonth, mMaxDay, mMaxHour, 0)
                        val limitDateEnd = mCalendar.time
                        TimePickHelper.picker(
                            context = mContext,
                            timeType = CustomDatePicker.Type.DH,
                            limitMin = limitDateStart,
                            limitMax = limitDateEnd,
                            selected = nowDate,
                            onDateListener = onDateListener
                        )
                    }
                    7 -> {
                        mCalendar.set(nowYear, nowMonth, nowDay, 0, 0)
                        val limitDateStart = mCalendar.time

                        val mMaxHour = mCalendar.getActualMaximum(Calendar.HOUR_OF_DAY)
                        mCalendar.set(nowYear, nowMonth, nowDay, mMaxHour, 59)
                        val limitDateEnd = mCalendar.time
                        TimePickHelper.picker(
                            context = mContext,
                            timeType = CustomDatePicker.Type.HM,
                            limitMin = limitDateStart,
                            limitMax = limitDateEnd,
                            selected = nowDate,
                            onDateListener = onDateListener
                        )
                    }
                    8 -> {
                        mCalendar.set(2020, 2 - 1, 2, 2, 2)
                        val limitDate = mCalendar.time

                        mCalendar.set(Calendar.YEAR, 2020 - 1)
                        val limitDateStart = mCalendar.time

                        mCalendar.set(Calendar.YEAR, 2020 + 1)
                        val limitDateEnd = mCalendar.time

                        TimePickHelper.picker(
                            context = mContext,
                            timeType = CustomDatePicker.Type.YMDHM,
                            limitMin = limitDateStart,
                            limitMax = limitDateEnd,
                            selected = limitDate,
                            onDateListener = onDateListener
                        )
                    }
                    9 -> {
                        mCalendar.set(Calendar.YEAR, nowYear - 1)
                        val limitDateStart = mCalendar.time

                        mCalendar.set(Calendar.YEAR, nowYear + 1)
                        val limitDateEnd = mCalendar.time

                        TimePickHelper.picker(
                            context = mContext,
                            timeType = CustomDatePicker.Type.YMDHM,
                            limitMin = limitDateStart,
                            limitMax = limitDateEnd,
                            onDateListener = onDateListener
                        )
                    }
                    10 -> {
                        mCalendar.set(2020, 2 - 1, 2, 2, 2)
                        val limitDate = mCalendar.time

                        mCalendar.set(Calendar.YEAR, 2020 - 5)
                        val limitDateStart = mCalendar.time

                        mCalendar.set(Calendar.YEAR, 2020 + 5)
                        val limitDateEnd = mCalendar.time

                        TimePickHelper.picker(
                            context = mContext,
                            timeType = CustomDatePicker.Type.YMDHM,
                            limitMin = limitDateStart,
                            limitMax = limitDateEnd,
                            selected = limitDate,
                            onDateListener = onDateListener
                        )
                    }
                    11 -> {
                        mCalendar.set(Calendar.YEAR, nowYear - 5)
                        val limitDateStart = mCalendar.time

                        mCalendar.set(Calendar.YEAR, nowYear + 5)
                        val limitDateEnd = mCalendar.time

                        TimePickHelper.picker(
                            context = mContext,
                            timeType = CustomDatePicker.Type.YMDHM,
                            limitMin = limitDateStart,
                            limitMax = limitDateEnd,
                            selected = nowDate,
                            onDateListener = onDateListener
                        )
                    }
                    12 -> {
                        mCalendar.set(2020, 2 - 1, 2, 2, 2)
                        val limitDate = mCalendar.time

                        mCalendar.set(Calendar.YEAR, 2020 - 5)
                        val limitDateStart = mCalendar.time

                        TimePickHelper.picker(
                            context = mContext,
                            timeType = CustomDatePicker.Type.YMDHM,
                            limitMin = limitDateStart,
                            limitMax = limitDate,
                            selected = limitDate,
                            onDateListener = onDateListener
                        )
                    }
                    13 -> {
                        mCalendar.set(Calendar.YEAR, nowYear - 5)
                        val limitDateStart = mCalendar.time

                        TimePickHelper.picker(
                            context = mContext,
                            timeType = CustomDatePicker.Type.YMDHM,
                            limitMin = limitDateStart,
                            limitMax = nowDate,
                            selected = nowDate,
                            onDateListener = onDateListener
                        )
                    }
                    14 -> {
                        mCalendar.set(2020, 2 - 1, 2, 2, 2)
                        val limitDate = mCalendar.time

                        mCalendar.set(Calendar.YEAR, 2020 + 5)
                        val limitDateEnd = mCalendar.time

                        TimePickHelper.picker(
                            context = mContext,
                            timeType = CustomDatePicker.Type.YMDHM,
                            limitMin = limitDate,
                            limitMax = limitDateEnd,
                            selected = limitDate,
                            onDateListener = onDateListener
                        )
                    }
                    15 -> {
                        mCalendar.set(Calendar.YEAR, nowYear + 5)
                        val limitDateEnd = mCalendar.time

                        TimePickHelper.picker(
                            context = mContext,
                            timeType = CustomDatePicker.Type.YMDHM,
                            limitMin = nowDate,
                            limitMax = limitDateEnd,
                            selected = nowDate,
                            onDateListener = onDateListener
                        )
                    }
                    16 -> {
                        mCalendar.set(2020, 2 - 1, 2, 2, 2)
                        val limitDate = mCalendar.time
                        mCalendar.set(Calendar.YEAR, 2020 - 5)
                        val limitDateStart = mCalendar.time
                        mCalendar.set(Calendar.YEAR, 2020 + 1)
                        val limitDateEnd = mCalendar.time

                        TimePickHelper.picker(
                            context = mContext,
                            timeType = CustomDatePicker.Type.YMDHM,
                            limitMin = limitDateStart,
                            limitMax = limitDateEnd,
                            selected = limitDate,
                            onDateListener = onDateListener
                        )
                    }
                    17 -> {
                        mCalendar.set(Calendar.YEAR, nowYear - 5)
                        val limitDateStart = mCalendar.time
                        mCalendar.set(Calendar.YEAR, nowYear + 1)
                        val limitDateEnd = mCalendar.time

                        TimePickHelper.picker(
                            context = mContext,
                            timeType = CustomDatePicker.Type.YMDHM,
                            limitMin = limitDateStart,
                            limitMax = limitDateEnd,
                            selected = nowDate,
                            onDateListener = onDateListener
                        )
                    }
                    18 -> {
                        mCalendar.set(2020, 2 - 1, 2, 2, 2)
                        val limitDate = mCalendar.time

                        mCalendar.set(Calendar.YEAR, 2020 + 1)
                        val limitDateStart = mCalendar.time

                        mCalendar.set(Calendar.YEAR, 2020 - 1)
                        val limitDateEnd = mCalendar.time

                        TimePickHelper.picker(
                            context = mContext,
                            timeType = CustomDatePicker.Type.YMDHM,
                            limitMin = limitDateStart,
                            limitMax = limitDateEnd,
                            selected = limitDate,
                            onDateListener = onDateListener
                        )
                    }
                }
            }
        })
    }

    private val onDateListener = object : OnDateListener {
        override fun setDate(date: Date?) {
            date?.let {
                timepicker_picked.text =
                    DateUtils.format(date = it, timeType = DateUtils.TimeType.YMDHMS)
            }
        }
    }
}