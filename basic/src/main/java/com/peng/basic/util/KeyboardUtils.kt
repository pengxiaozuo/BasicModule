package com.peng.basic.util

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout


/**
 * 软键盘工具类
 */
object KeyboardUtils {

    private var onGlobalLayoutListener: OnGlobalLayoutListener? = null
    private var onSoftInputChangedListener: OnSoftInputChangedListener? = null
    private var sDecorViewInvisibleHeightPre: Int = 0


    /**
     * 显示软键盘
     *
     * @param view  焦点View
     * @param flags
     */
    fun showSoftInput(activity: Activity, view: View? = null, flags: Int = InputMethodManager.SHOW_FORCED) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        var focusView = view
        if (focusView == null) {
            focusView = activity.currentFocus
        }

        if (focusView == null) {
            focusView = View(activity)
        }

        focusView.isFocusable = true
        focusView.isFocusableInTouchMode = true
        focusView.requestFocus()
        val result = imm.showSoftInput(focusView, flags, object : ResultReceiver(Handler()) {
            override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
                if (resultCode == InputMethodManager.RESULT_UNCHANGED_HIDDEN || resultCode == InputMethodManager.RESULT_HIDDEN) {
                    toggleSoftInput(activity)
                }
            }
        })
        if (!result && !isSoftInputVisible(activity)) {
            toggleSoftInput(activity)
        }
    }


    /**
     * 隐藏软键盘
     *
     * @param view 焦点view
     */
    fun hideSoftInput(activity: Activity, view: View? = null) {
        var focusView = view
        if (focusView == null) {
            focusView = activity.currentFocus
        }
        if (focusView == null) {
            focusView = View(activity)
        }
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val result = imm.hideSoftInputFromWindow(focusView.windowToken, 0, object : ResultReceiver(Handler()) {
            override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
                if (resultCode == InputMethodManager.RESULT_UNCHANGED_SHOWN || resultCode == InputMethodManager.RESULT_SHOWN) {
                    toggleSoftInput(activity)
                }
            }
        })

        if (!result && isSoftInputVisible(activity)) {
            toggleSoftInput(activity)
        }
    }

    /**
     * 切换软键盘显示隐藏
     */
    fun toggleSoftInput(activity: Activity) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    /**
     * 软键盘是否可见
     *
     * @param activity
     */
    fun isSoftInputVisible(activity: Activity): Boolean {
        return getDecorViewInvisibleHeight(activity) > 0
    }

    private var sDecorViewDelta = 0

    private fun getDecorViewInvisibleHeight(activity: Activity): Int {
        val decorView = activity.window.decorView ?: return sDecorViewInvisibleHeightPre
        val outRect = Rect()
        decorView.getWindowVisibleDisplayFrame(outRect)
        val delta = Math.abs(decorView.bottom - outRect.bottom)
        if (delta <= getNavBarHeight()) {
            sDecorViewDelta = delta
            return 0
        }
        return delta - sDecorViewDelta
    }

    private fun getStatusBarHeight(): Int {
        val resources = Resources.getSystem()
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return resources.getDimensionPixelSize(resourceId)
    }

    private fun getNavBarHeight(): Int {
        val res = Resources.getSystem()
        val resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android")
        return if (resourceId != 0) {
            res.getDimensionPixelSize(resourceId)
        } else {
            0
        }
    }

    /**
     * 注册软键盘状态改变listener
     *
     */
    fun registerSoftInputChangedListener(
        activity: Activity,
        listener: OnSoftInputChangedListener
    ) {
        val flags = activity.window.attributes.flags
        if (flags and WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS != 0) {
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }
        val contentView = activity.findViewById<FrameLayout>(android.R.id.content)
        sDecorViewInvisibleHeightPre = getDecorViewInvisibleHeight(activity)
        onSoftInputChangedListener = listener
        onGlobalLayoutListener = OnGlobalLayoutListener {
            if (onSoftInputChangedListener != null) {
                val height = getDecorViewInvisibleHeight(activity)
                if (sDecorViewInvisibleHeightPre !== height) {
                    onSoftInputChangedListener?.onSoftInputChanged(height)
                    sDecorViewInvisibleHeightPre = height
                }
            }
        }
        contentView.viewTreeObserver
            .addOnGlobalLayoutListener(onGlobalLayoutListener)
    }

    /**
     * 解注册软键盘状态改变listener
     *
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    fun unregisterSoftInputChangedListener(activity: Activity) {
        val contentView = activity.findViewById<View>(android.R.id.content)
        contentView.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalLayoutListener)
        onSoftInputChangedListener = null
        onGlobalLayoutListener = null
    }

    interface OnSoftInputChangedListener {
        fun onSoftInputChanged(height: Int)
    }
}