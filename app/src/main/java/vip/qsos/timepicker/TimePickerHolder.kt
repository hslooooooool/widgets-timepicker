package vip.qsos.timepicker

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_time_picker.view.*

/**
 * @author : 华清松
 *
 * 时间选择器案例列表项
 */
class TimePickerHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun setData(data: String) {
        itemView.item_timepicker_name.text = data
    }
}