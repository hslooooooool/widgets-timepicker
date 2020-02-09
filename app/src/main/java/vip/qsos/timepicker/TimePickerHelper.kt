package vip.qsos.timepicker

import android.content.Context

/**
 * @author : 华清松
 */
object TimePickerHelper {

    fun picker(
        context: Context,
        onDateListener: OnDateListener
    ) {
        TimePicker.selectDate(
            context = context,
            limitMin = null, limitMax = null, selected = null,
            showDayTime = true, showSpecificTime = true,
            onDateListener = onDateListener
        )
    }
}