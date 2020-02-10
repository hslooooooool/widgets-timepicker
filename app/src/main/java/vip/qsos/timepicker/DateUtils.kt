package vip.qsos.timepicker

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author : 华清松
 *
 * 时间格式转换 工具类
 */
@SuppressLint("SimpleDateFormat")
object DateUtils {

    enum class TimeType(val type: String) {
        Y("yyyy"),
        YM("yyyy-MM"),
        YMD("yyyy-MM-dd"),
        YMDH("yyyy-MM-dd HH"),
        YMDHM("yyyy-MM-dd HH:mm"),
        YMDHMS("yyyy-MM-dd HH:mm:ss"),

        MD("MM-dd"),
        MDH("MM-dd HH"),
        MDHM("MM-dd HH:mm"),
        MDHMS("MM-dd HH:mm:ss"),

        HM("HH:mm"),
        HMS("HH:mm:ss");
    }

    /**将时间转为 String */
    fun format(
        millis: Long? = null,
        date: Date?,
        pattern: String? = null,
        timeType: TimeType? = TimeType.YMDHMS
    ): String {
        if ((millis == null && date == null) || (timeType == null && pattern == null)) {
            return ""
        }
        val mFormat: SimpleDateFormat = when {
            timeType != null -> {
                SimpleDateFormat(timeType.type, Locale.CHINA)
            }
            else -> {
                SimpleDateFormat(pattern ?: TimeType.YMDHMS.type, Locale.CHINA)
            }
        }
        val mDate = when {
            millis != null -> Date(millis)
            else -> date
        }!!
        return mFormat.format(mDate)
    }
}