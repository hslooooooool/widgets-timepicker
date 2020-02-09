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
        "普通时间格式\n格式：年月日时分\n限制：设定时间上下100年",

        "定制时间格式\n格式：年月日时分\n限制：设定时间上下100年",
        "定制时间格式\n格式：年月日时\n限制：设定时间上下100年",
        "定制时间格式\n格式：年月日\n限制：设定时间上下100年",
        "定制时间格式\n格式：年月\n限制：设定时间上下100年",
        "定制时间格式\n格式：年\n限制：设定时间上下100年",

        "定制时间格式\n格式：日时分\n限制：设定时间上下100年",
        "定制时间格式\n格式：日时\n限制：设定时间上下100年",
        "定制时间格式\n格式：时分\n限制：设定时间上下100年",

        "限制时间格式\n格式：年月日时分\n限制：设定时间上下1年",
        "限制时间格式\n格式：年月日时分\n限制：设定时间上下5年",
        "限制时间格式\n格式：年月日时分\n限制：设定时间往前5年",
        "限制时间格式\n格式：年月日时分\n限制：设定时间往后5年",

        "限制时间格式\n格式：年月日时分\n限制：当前时间上下1年",
        "限制时间格式\n格式：年月日时分\n限制：当前时间上下5年",
        "限制时间格式\n格式：年月日时分\n限制：当前时间往前5年",
        "限制时间格式\n格式：年月日时分\n限制：当前时间往后5年"
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
                when (p) {
                    1 -> {
                        TimePickerHelper.picker(context = mContext, onDateListener = onDateListener)
                    }
                }
            }
        })
    }

    private val onDateListener = object : OnDateListener {
        override fun setDate(type: Int?, date: Date?) {
            timepicker_picked.text =
                DateUtils.format(date = date, timeType = DateUtils.TimeType.YMDHM)
        }
    }
}