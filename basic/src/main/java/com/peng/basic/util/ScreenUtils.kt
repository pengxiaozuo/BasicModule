package com.peng.basic.util

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.view.WindowManager


object ScreenUtils {

    /**
     * 获取屏幕宽度
     */
    @JvmStatic
    fun getScreenWidth(context: Context): Int {
        return context.resources.displayMetrics.widthPixels
    }

    /**
     * 获取屏幕高度
     */
    @JvmStatic
    fun getScreenHeight(context: Context): Int {
        return context.resources.displayMetrics.heightPixels
    }

    /**
     * 设置全屏
     */
    @JvmStatic
    fun setFullSreen(activity: Activity) {
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    /**
     * 设置取消全屏
     */
    @JvmStatic
    fun setNoFullSreen(activity: Activity) {
        activity.window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }


    /**
     * 截屏
     */
    @JvmStatic
    fun screenShot(activity: Activity, isDeleteStatusBar: Boolean = false): Bitmap? {
        val decorView = activity.window.decorView
        decorView.isDrawingCacheEnabled = true
        decorView.buildDrawingCache()
        val bmp = decorView.drawingCache ?: return null
        val width = getScreenWidth(activity)
        val height = getScreenHeight(activity)

        val ret: Bitmap

        ret = if (isDeleteStatusBar) {
            val stateBarHeight = BarUtils.getStateBarHeight(activity)
            Bitmap.createBitmap(bmp, 0, stateBarHeight, width, height - stateBarHeight)
        } else {
            Bitmap.createBitmap(bmp, 0, 0, width, height)
        }
        decorView.destroyDrawingCache()
        return ret
    }

}