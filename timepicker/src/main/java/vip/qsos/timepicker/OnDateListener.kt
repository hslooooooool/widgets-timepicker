package vip.qsos.timepicker

import java.util.*

/**
 * @author 华清松
 *
 * 时间选择回调接口
 */
interface OnDateListener {
    /**
     * 选择时间回调
     *
     * @param date 选择的时间
     */
    fun setDate(date: Date?)
}
