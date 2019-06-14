package com.peng.basic.util

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * 时间工具类
 */
object TimeUtils {

    /**
     * 默认格式化格式
     */
    val defaultFormat: SimpleDateFormat
        get() {
            return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        }

    /**
     * 毫秒格式化为字符串
     */
    @JvmStatic
    @JvmOverloads
    fun millis2String(millis: Long, format: DateFormat = defaultFormat): String {
        return format.format(Date(millis))
    }


    /**
     * 字符串时间转换为毫秒值
     */
    @JvmStatic
    @JvmOverloads
    fun string2Millis(time: String, format: DateFormat = defaultFormat): Long {
        val date = string2Date(time, format)
        return date.time
    }

    /**
     * 字符串转换为Date
     */
    @JvmStatic
    @JvmOverloads
    fun string2Date(time: String, format: DateFormat = defaultFormat): Date {
        return format.parse(time)
    }

    /**
     * Date格式化字符串
     */
    @JvmStatic
    @JvmOverloads
    fun date2String(date: Date, format: DateFormat = defaultFormat): String {
        return format.format(date)
    }

    /**
     * 毫秒值转化为Date
     */
    @JvmStatic
    fun millis2Date(millis: Long): Date {
        return Date(millis)
    }

    /**
     * Date值转化为毫秒值
     */
    @JvmStatic
    fun date2Millis(date: Date): Long {
        return date.time
    }

    /**
     * 将已经格式化的时间字符串格式化为其他格式
     */
    @JvmStatic
    fun format2Format(time: String, scr: DateFormat, desc: DateFormat): String {
        val date = scr.parse(time)
        return desc.format(time)
    }

    /**
     * 判断是否闰年
     */
    @JvmStatic
    @JvmOverloads
    fun isLeapYear(time: String, format: DateFormat = defaultFormat): Boolean {

        return isLeapYear(string2Date(time, format))
    }

    /**
     * 判断是否闰年
     */
    @JvmStatic
    fun isLeapYear(millis: Long): Boolean {
        return isLeapYear(millis2Date(millis))
    }

    /**
     * 判断是否闰年
     */
    @JvmStatic
    fun isLeapYear(date: Date): Boolean {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return isLeapYear(calendar.get(Calendar.YEAR))
    }

    /**
     * 判断是否闰年
     */
    @JvmStatic
    fun isLeapYear(year: Int): Boolean {
        return year % 4 == 0 && year % 100 != 0 || year % 400 == 0
    }
}

fun Date.format(pattern: String? = null): String =
    TimeUtils.date2String(
        this,
        if (pattern.isNullOrEmpty()) TimeUtils.defaultFormat else SimpleDateFormat(pattern, Locale.getDefault())
    )

fun Date.format(format: DateFormat?): String =
    TimeUtils.date2String(
        this,
        format ?: TimeUtils.defaultFormat
    )

fun Long.millisFormat(pattern: String? = null): String =
    TimeUtils.date2String(
        Date(this),
        if (pattern.isNullOrEmpty()) TimeUtils.defaultFormat else SimpleDateFormat(pattern, Locale.getDefault())
    )

fun Long.millisFormat(format: DateFormat?): String =
    TimeUtils.date2String(
        Date(this),
        format ?: TimeUtils.defaultFormat
    )