package vip.qsos.timepicker

import android.content.Context
import java.util.*

/**
 * @author 华清松
 *
 * 时间选择器
 */
object TimePickHelper {

    /**
     * 具体时间选择
     *
     * @param context
     * @param timeType 时间格式，默认【年月日时分】
     * @param loop 选择器是否循环滚动
     * @param span 时间上下限制可选范围，单位【分钟】
     * @param selected 已选择的时间
     * @param limitMin 最小可选时间
     * @param limitMax 最大可选时间
     * @param onDateListener 选择结果回调
     */
    fun picker(
        context: Context,
        timeType: CustomDatePicker.Type = CustomDatePicker.Type.YMDHM,
        loop: Boolean = true,
        span: Int = 10,
        selected: Date? = null,
        limitMin: Date? = null, limitMax: Date? = null,
        onDateListener: OnDateListener
    ) {
        var mSelectDate = selected ?: Date()
        val mCalendar = Calendar.getInstance()
        mCalendar.time = mSelectDate
        mCalendar.set(Calendar.SECOND, 0)
        mSelectDate = mCalendar.time

        /**可选范围*/
        var startDate: Date
        var endDate: Date
        /**将要设置开始时间，那么开始时间不得大于完结时间，如果结束时间为空，则随意设置，但不得小于当前时间，但如果开始时间不为空，则判断开始与当前时间大小*/
        if (limitMax == null) {
            mCalendar.set(Calendar.YEAR, mCalendar.get(Calendar.YEAR) - span)
            startDate = limitMin ?: mCalendar.time
            mCalendar.set(Calendar.YEAR, mCalendar.get(Calendar.YEAR) + 2 * span)
            endDate = mCalendar.time
        } else {
            mCalendar.time = limitMax
            /**完结时间不为空，开始时间不得大于完结时间，则最大可设置时间不得大于完结时间*/
            endDate = limitMax
            mCalendar.set(Calendar.YEAR, mCalendar.get(Calendar.YEAR) - span)
            startDate = limitMin ?: mCalendar.time
        }
        if (startDate > mSelectDate) {
            startDate = mSelectDate
        }
        if (endDate < mSelectDate) {
            endDate = mSelectDate
        }
        /**创建时间选择器*/
        val mPicker = CustomDatePicker.Builder(
            context = context,
            type = timeType,
            startDate = startDate,
            endDate = endDate,
            selectDate = mSelectDate,
            loop = loop,
            onDateListener = onDateListener
        )
        mPicker.build()?.show()
    }
}