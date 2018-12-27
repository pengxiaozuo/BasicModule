package com.peng.basic.util

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout


object BarUtils {

    /**
     * 获取状态栏高度
     */
    @JvmStatic
    fun getStateBarHeight(context: Context): Int {
        val resourceId = context.resources.getIdentifier("status_bar_height",
                "dimen", "android")
        return context.resources.getDimensionPixelSize(resourceId)
    }

    /**
     * 获取ActionBar高度
     */
    @JvmStatic
    fun getActionBarHeight(context: Context): Int {
        val tv = TypedValue()
        return if (context.theme.resolveAttribute(android.R.attr.actionBarSize, tv,
                        true)) {
            TypedValue.complexToDimensionPixelSize(tv.data, context.resources.displayMetrics)
        } else 0
    }

    /**
     * 获取导航栏高度
     */
    @JvmStatic
    fun getNavBarHeight(context: Context): Int {
        val res = context.resources
        val resourceId = res.getIdentifier("navigation_bar_height",
                "dimen", "android")
        return if (resourceId != 0) {
            res.getDimensionPixelSize(resourceId)
        } else {
            0
        }
    }

    /**
     * 设置状态了暗色模式
     */
    @JvmStatic
    fun setDarkMode(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
    }

    /**
     * 设置状态了亮色模式
     */
    @JvmStatic
    fun setLightMode(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.window.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
    }


    /**
     * 设置状态了颜色
     */
    @JvmStatic
    fun setBarColor(activity: Activity, color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            activity.window.statusBarColor = color
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            val decorView = activity.window.decorView as ViewGroup

            val statusBarView = createStatusBar(activity, color)

            decorView.addView(statusBarView)

            val contentView = activity.findViewById(Window.ID_ANDROID_CONTENT) as ViewGroup

            contentView.fitsSystemWindows = true
            contentView.clipToPadding = true
        }

    }

    @JvmStatic
    private fun createStatusBar(activity: Activity, color: Int): View {
        val statusBarView = View(activity)
        val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStateBarHeight(activity))
        statusBarView.layoutParams = params
        statusBarView.setBackgroundColor(color)
        return statusBarView
    }


    /**
     * 透明状态栏
     */
    @JvmStatic
    fun setTransparentMode(activity: Activity) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val decorView = activity.window.decorView
            val option = (
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    )
            decorView.systemUiVisibility = option
            activity.window.statusBarColor = Color.TRANSPARENT
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            val contentView: ViewGroup = activity.window.decorView
                    .findViewById(Window.ID_ANDROID_CONTENT)
            contentView.getChildAt(0).fitsSystemWindows = false
        }

    }

    /**
     * 沉侵式
     */
    @JvmStatic
    fun setImmersiveMode(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val decorView = activity.window.decorView
            decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }

    }


}