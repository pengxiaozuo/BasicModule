package com.peng.basic.util

import android.content.Context
import android.net.ConnectivityManager
import android.support.annotation.RequiresPermission

/**
 * 网络工具类
 */
object NetUtils {

    /**
     * no network
     */
    const val NETWORK_NO = -1
    /**
     * wifi
     */
    const val NETWORK_WIFI = 1
    /**
     * "移动网络
     */
    const val NETWORK_MOBILE = 2


    /**
     * 未知
     */
    const val NETWORK_UNKNOWN = 3

    /**
     * 获取网络类型
     *
     * @return [NETWORK_WIFI] ,[NETWORK_MOBILE] ,[NETWORK_UNKNOWN]
     */
    @RequiresPermission("android.permission.ACCESS_NETWORK_STATE")
    @JvmStatic
    fun getType(context: Context): Int {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val ni = cm.activeNetworkInfo
        return when (ni.type) {
            ConnectivityManager.TYPE_WIFI -> NETWORK_WIFI
            ConnectivityManager.TYPE_MOBILE -> NETWORK_MOBILE
            else -> NETWORK_UNKNOWN
        }
    }

    /**
     * 当前是wifi网络
     */
    @RequiresPermission("android.permission.ACCESS_NETWORK_STATE")
    @JvmStatic
    fun isWifi(context: Context): Boolean {
        return getType(context) == NETWORK_WIFI
    }

    /**
     * 网络是否可用
     */
    @RequiresPermission("android.permission.ACCESS_NETWORK_STATE")
    @JvmStatic
    fun isAvailable(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = cm.activeNetworkInfo
        return info != null && info.isAvailable
    }
}