package com.peng.basic.util

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.Signature


object AppUtils {
    @JvmStatic
    fun isSystemApp(context: Context, packageName: String): Boolean {
        if (packageName.isEmpty()) return false
        return try {
            val pm = context.packageManager
            val ai = pm.getApplicationInfo(packageName, 0)
            ai != null && ai.flags and ApplicationInfo.FLAG_SYSTEM != 0
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            false
        }
    }

    /**
     * 判断App是否是Debug版本
     */
    @JvmStatic
    fun isAppDebug(context: Context, packageName: String): Boolean {
        if (packageName.isEmpty()) return false
        return try {
            val pm = context.packageManager
            val ai = pm.getApplicationInfo(packageName, 0)
            ai != null && ai.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            false
        }

    }

    /**
     * 获取App签名
     */
    @JvmStatic
    fun getAppSignature(context: Context): Array<Signature>? {
        return getAppSignature(context, context.packageName)
    }

    /**
     * 获取App签名
     */
    @JvmStatic
    @SuppressLint("PackageManagerGetSignatures")
    fun getAppSignature(context: Context, packageName: String): Array<Signature>? {
        if (packageName.isEmpty()) return null
        return try {
            val pm = context.packageManager
            val pi = pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            pi?.signatures
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            null
        }

    }

    /**
     * 获取应用签名的的SHA1值
     *
     * @return 应用签名的SHA1字符串, 比如：58:E1:C4:13:3F:74:41:EC:3D:2C:27:02:70:A1:48:02:DA:47:BA:0E
     */
    @JvmStatic
    fun getAppSignatureSHA1(context: Context): String? {
        return getAppSignatureSHA1(context, context.packageName)
    }

    /**
     * 获取应用签名的的SHA1值
     *
     * @return 应用签名的SHA1字符串, 比如：58:E1:C4:13:3F:74:41:EC:3D:2C:27:02:70:A1:48:02:DA:47:BA:0E
     */
    @JvmStatic
    fun getAppSignatureSHA1(context: Context, packageName: String): String? {
        val signature = getAppSignature(context, packageName) ?: return null
        return SignUtils.sha1ToString(signature[0].toByteArray())
                ?.replace("(?<=[0-9A-F]{2})[0-9A-F]{2}".toRegex(), ":$0")
    }

    /**
     * 判断App是否处于前台
     */
    @JvmStatic
    fun isAppForeground(context: Context): Boolean {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val infos = manager.runningAppProcesses
        if (infos == null || infos.size == 0) return false
        for (info in infos) {
            if (info.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return info.processName == context.packageName
            }
        }
        return false
    }

}