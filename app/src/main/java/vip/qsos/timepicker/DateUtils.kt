package vip.qsos.timepicker

import android.annotation.SuppressLint
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author : 华清松
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
                SimpleDateFormat(pattern, Locale.CHINA)
            }
        }
        val mDate = when {
            millis != null -> Date(millis)
            else -> date
        }!!
        return mFormat.format(mDate)
    }

    /**时间转化为过去时长:刚刚、59分钟前、23小时前、昨天、29天前、10-10*/
    fun getTimeDifference(date: Date): String {
        val nowDate = Date()
        var diff = nowDate.time - date.time
        diff /= 1000
        if (diff < 1) {
            return "刚刚"
        }
        diff /= 60
        if (diff < 5) {
            return "刚刚"
        }
        if (diff in 6..59) {
            return diff.toString() + "分钟前"
        }
        diff /= 60
        if (diff <= 24) {
            return diff.toString() + "小时前"
        }
        diff /= 24
        if (diff < 30) {
            return if (diff == 1L) {
                "昨天"
            } else diff.toString() + "天前"
        }
        val dfDate = SimpleDateFormat("MM-dd")
        return dfDate.format(date)
    }

    /**时间转化为过去时长：刚刚、4:50、昨天、29天前、2019-10-10*/
    fun getTimeToNow(date: Date?): String {
        if (date == null) {
            return ""
        }
        val nowDate = Date()
        var diff = nowDate.time - date.time
        diff /= 1000
        if (diff < 1) {
            return "刚刚"
        }
        diff /= 60
        if (diff < 5) {
            return "刚刚"
        }
        if (diff in 6..59) {
            return SimpleDateFormat("HH:mm").format(date)
        }
        diff /= 60
        if (diff <= 24) {
            return SimpleDateFormat("HH:mm").format(date)
        }
        diff /= 24
        if (diff < 30) {
            return if (diff == 1L) {
                "昨天"
            } else diff.toString() + "天前"
        }
        val dfDate = SimpleDateFormat("yyyy-MM-dd")
        return dfDate.format(date)
    }

    /**将时间转为Date*/
    fun strToDate(strDate: String): Date? {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val pos = ParsePosition(0)
        return formatter.parse(strDate, pos)
    }

    /**获取年、月*/
    fun formatTimeToStringArray(millis: Long? = null, date: Date? = null): Array<String> {
        val time = arrayOf("", "", "", "", "", "")
        if (millis == null && date == null) {
            return time
        }
        val mDate = date ?: Date(millis!!)
        val mY = String.format("%tY", mDate)
        val mM = String.format("%tM", mDate)
        val md = String.format("%td", mDate)
        val mH = String.format("%tH", mDate)
        val mm = String.format("%tm", mDate)
        val ms = String.format("%ts", mDate)
        time[0] = mY
        time[1] = mM
        time[2] = md
        time[3] = mH
        time[4] = mm
        time[5] = ms
        return time
    }

    /**
     * 验证字符串是否是一个合法的日期格式
     *
     * @param date     时间
     * @param template 验证的格式
     * @return 是否合法
     */
    fun validIsDate(date: String, template: String): Boolean {
        var convertSuccess = true
        // 指定日期格式
        val format = SimpleDateFormat(template, Locale.CHINA)
        try {
            // 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2015/02/29会被接受，并转换成2015/03/01
            format.isLenient = false
            format.parse(date)
        } catch (e: Exception) {
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            convertSuccess = false
        }
        return convertSuccess
    }

    /**将时间转化为X天X小时X分钟X秒*/
    fun formatTimeToLongArray(
        millis: Long? = null,
        date: Date? = null
    ): Array<Long> {
        val time = arrayOf(0L, 0L, 0L, 0L)
        if (millis == null && date == null) {
            return time
        }
        var mTime = millis ?: date!!.time
        //天数
        time[0] = mTime / (1000 * 60 * 60 * 24)
        //减去整天后剩余
        mTime %= (1000 * 60 * 60 * 24)
        //小时
        time[1] = mTime / (1000 * 60 * 60)
        //减去剩余小时后时间
        mTime %= (1000 * 60 * 60)
        //分钟
        time[2] = mTime / (1000 * 60)
        //减去剩余分钟后时间
        mTime %= (1000 * 60)
        //秒
        time[3] = mTime / 1000
        return time
    }

}