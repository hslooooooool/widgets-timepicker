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
     * @param type 时间类型,0不选，1具体，2相对
     * @param date 选择的时间
     */
    fun setDate(type: Int?, date: Date?)
}
