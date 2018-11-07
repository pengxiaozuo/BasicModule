package com.peng.basic.util

import java.util.regex.Pattern

object RegexUtils {

    /**
     * 是否手机号，需要去除国际区号
     */
    @JvmStatic
    fun isMobile(str: String): Boolean {
        val regex = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|(147))\\d{8}$"
        return isMatch(regex, str)
    }

    /**
     * 是否固定电话号
     */
    @JvmStatic
    fun isTel(str: String): Boolean {
        val regex = "^0\\d{2,3}[- ]?\\d{7,8}"
        return isMatch(regex, str)
    }

    /**
     * 是否邮箱
     */
    @JvmStatic
    fun isEmail(str: String): Boolean {
        val regex = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$"
        return isMatch(regex, str)
    }

    /**
     * 是否网址
     */
    @JvmStatic
    fun isUrl(str: String): Boolean {
        val regex = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?"
        return isMatch(regex, str)
    }

    /**
     * 是否身份证号
     */
    @JvmStatic
    fun isIdCard(str: String): Boolean {
        val regex = "(^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$|^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|x|X)$)"
        return isMatch(regex, str)
    }

    /**
     * 是否银行卡号
     */
    @JvmStatic
    fun isBankCard(str: String): Boolean {
        val regex = "^\\d{16,19}$|^\\d{6}[- ]\\d{10,13}$|^\\d{4}[- ]\\d{4}[- ]\\d{4}[- ]\\d{4,7}$"
        return isMatch(regex, str)
    }

    /**
     * 是否汉字
     */
    @JvmStatic
    fun isCHZ(str: String): Boolean {
        val regex = "^[\\u4e00-\\u9fa5]+$"
        return isMatch(regex, str)
    }

    /**
     * 是否IPV4地址
     */
    @JvmStatic
    fun isIPV4(str: String): Boolean {
        val regex = "((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)"
        return isMatch(regex, str)
    }


    /**
     * 是否匹配
     */
    @JvmStatic
    fun isMatch(regex: String, str: String): Boolean {
        val p = Pattern.compile(regex)
        val matcher = p.matcher(str)
        return matcher.matches()
    }
}