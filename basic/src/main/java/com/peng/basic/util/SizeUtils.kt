package com.peng.basic.util

import android.content.Context

/**
 * 单位转换工具类
 */
object SizeUtils {

    /**
     * dp 转 px
     */
    @JvmStatic
    fun dp2px(context: Context, value: Float): Float {
        val scale = context.resources.displayMetrics.density
        return value * scale + 0.5F
    }

    /**
     * sp 转 px
     */
    @JvmStatic
    fun sp2px(context: Context, value: Float): Float {
        val scale = context.resources.displayMetrics.scaledDensity
        return value * scale + 0.5F
    }

    /**
     * px 转 dp
     */
    @JvmStatic
    fun px2dp(context: Context, value: Float): Float {
        val scale = context.resources.displayMetrics.density
        return value / scale + 0.5F
    }

    /**
     * px 转 sp
     */
    @JvmStatic
    fun px2sp(context: Context, value: Float): Float {
        val scale = context.resources.displayMetrics.scaledDensity
        return value / scale + 0.5F
    }
}

fun Context.dp2px(value: Float): Float = SizeUtils.dp2px(this, value)
fun Context.sp2px(value: Float): Float = SizeUtils.sp2px(this, value)
fun Context.px2dp(value: Float): Float = SizeUtils.px2dp(this, value)
fun Context.px2sp(value: Float): Float = SizeUtils.px2sp(this, value)