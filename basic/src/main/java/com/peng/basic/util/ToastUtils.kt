package com.peng.basic.util

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast

/**
 * Toast工具类
 */
object ToastUtils {

    private var mToast: Toast? = null

    @SuppressLint("ShowToast")
    @JvmOverloads
    fun showToast(context: Context, text: String, duration: Int = Toast.LENGTH_SHORT) {
        if (mToast == null) {
            mToast = Toast.makeText(context.applicationContext, text, duration)
        } else {
            cancelToast()
            mToast = Toast.makeText(context.applicationContext, text, duration)
            mToast!!.setText(text)
            mToast!!.duration = duration
        }
        mToast!!.show()
    }

    fun cancelToast() {
        mToast?.cancel()
    }


}
