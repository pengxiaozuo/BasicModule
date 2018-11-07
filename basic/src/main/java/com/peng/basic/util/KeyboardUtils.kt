package com.peng.basic.util

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText


object KeyboardUtils {

    /**
     * 隐藏软键盘
     */
    @JvmStatic
    fun hideSoftInput(activity: Activity) {
        val view = activity.window.peekDecorView()
        if (view != null) {
            val inputManager = activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }


    /**
     * 隐藏软键盘
     */
    @JvmStatic
    fun hideSoftInput(context: Context, edit: EditText) {
        edit.clearFocus()
        val inputManager = context
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(edit.windowToken, 0)
    }

    /**
     * 显示软键盘
     */
    @JvmStatic
    fun showSoftInput(context: Context, edit: EditText) {
        edit.isFocusable = true
        edit.isFocusableInTouchMode = true
        edit.requestFocus()
        val inputManager = context
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.showSoftInput(edit, 0)
    }

    /**
     * 隐藏显示切换
     */
    @JvmStatic
    fun toggleSoftInput(context: Context, edit: EditText) {
        edit.isFocusable = true
        edit.isFocusableInTouchMode = true
        edit.requestFocus()
        val inputManager = context
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }
}