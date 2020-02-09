package vip.qsos.timepicker

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * @author : 华清松
 *
 * 时间选择器案例列表容器
 */
class TimePickerAdapter(private val list: List<String>) :
    RecyclerView.Adapter<TimePickerHolder>() {
    private var mOnItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onClick(p: Int, data: String)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.mOnItemClickListener = onItemClickListener
    }

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimePickerHolder {
        return TimePickerHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_time_picker, null)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: TimePickerHolder, position: Int) {
        holder.setData(list[position])
        holder.itemView.setOnClickListener {
            this.mOnItemClickListener?.onClick(position, list[position])
        }
    }

}