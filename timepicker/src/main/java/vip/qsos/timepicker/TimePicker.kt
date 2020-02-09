package vip.qsos.timepicker

import android.content.Context
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

/**
 * @author 华清松
 *
 * 时间选择器
 */
object TimePicker {

    /**
     * 具体时间选择
     *
     * @param context
     * @param showDayTime      是否显示日
     * @param showSpecificTime 是否显示时分
     * @param limitMin 最小可选时间
     * @param limitMax 最大可选时间
     * @param selected 默认选择的时间
     * @param onDateListener 选择结果回调
     */
    fun selectDate(
            context: Context,
            limitMin: Date? = null, limitMax: Date? = null, selected: Date? = null,
            showDayTime: Boolean = true,
            showSpecificTime: Boolean = true,
            onDateListener: OnDateListener
    ) {
        var selectDate = selected
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA)
        // 设置可选年范围
        val span = 3L
        // 获取当前可选时间
        val nowDate = Date()
        /*可选范围*/
        val sDate: Date
        val eDate: Date
        /*将要设置启动时间，那么启动时间不得大于完结时间，如果完结时间为空，则随意设置，但不得小于当前时间，但如果开始时间不为空，则判断开始与当前时间大小*/
        if (limitMax == null) {
            sDate = limitMin ?: Date(nowDate.time - span * 365 * 60 * 24 * 1000 * 60L)
            eDate = Date(sDate.time + (if (limitMin == null) 2 else 1) * span * 365 * 60 * 24 * 1000 * 60L)
        } else {
            /*完结时间不为空，开始时间不得大于完结时间，则最大可设置时间不得大于完结时间*/
            eDate = Date(limitMax.time)
            sDate = limitMin ?: Date(eDate.time - span * 365 * 60 * 24 * 1000 * 60L)
        }
        /*构建时间选择窗口*/
        val customDatePicker = CustomDatePicker(
            context,
            sdf,
            sdf.format(sDate),
            sdf.format(eDate),
            onDateListener
        )

        if (selectDate == null || selectDate.time > eDate.time || selectDate.time < sDate.time && abs(selectDate.time - sDate.time) > 1000) {
            selectDate = nowDate
        }
        customDatePicker.showDayTime(showDayTime, showSpecificTime)
        customDatePicker.setIsLoop(false)
        customDatePicker.show(sdf.format(selectDate))
    }
}