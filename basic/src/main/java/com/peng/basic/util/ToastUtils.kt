package com.peng.basic.util

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

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

fun Context.toast(text: String?) {
    text?.let {
        ToastUtils.showToast(this, text)
    }
}

fun Context.toast(@StringRes resId: Int, vararg args: Any?) {
    ToastUtils.showToast(this, getString(resId, args))
}

fun Context.toastLong(text: String?) {
    text?.let {
        ToastUtils.showToast(this, text, Toast.LENGTH_LONG)
    }
}

fun Context.toastLong(@StringRes resId: Int, vararg args: Any?) {
    ToastUtils.showToast(this, getString(resId, args), Toast.LENGTH_LONG)
}


fun Fragment.toast(text: String?) {
    text?.let {
        activity?.let {
            ToastUtils.showToast(it, text)
        }
    }
}

fun Fragment.toast(@StringRes resId: Int, vararg args: Any?) {
    activity?.let {
        ToastUtils.showToast(it, getString(resId, args))
    }
}

fun Fragment.toastLong(text: String?) {
    text?.let {
        activity?.let {
            ToastUtils.showToast(it, text, Toast.LENGTH_LONG)
        }
    }
}

fun Fragment.toastLong(@StringRes resId: Int, vararg args: Any?) {
    activity?.let {
        ToastUtils.showToast(it, getString(resId, args), Toast.LENGTH_LONG)
    }
}