package com.peng.basic.util

import android.Manifest
import android.content.Context
import android.os.Build
import android.provider.Settings
import android.support.annotation.RequiresPermission
import android.telephony.TelephonyManager

object DeviceUtils {

    /**
     * 获取设备型号
     */
    @JvmStatic
    fun getUniqueSerialNumber(): String {
        val phoneName = Build.MODEL
        val manufacturer = Build.MANUFACTURER
        return manufacturer + "-" + phoneName + "-" + getSerialNumber()
    }


    /**
     * 获取设备的IMEI
     */
    @JvmStatic
    @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
    fun getDeviceIdIMEI(context: Context): String? {
        val id: String
        //android.telephony.TelephonyManager
        val mTelephony = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        id = if (mTelephony.deviceId != null) {
            mTelephony.deviceId
        } else {
            Settings.Secure.getString(context.applicationContext.contentResolver, Settings.Secure.ANDROID_ID)
        }
        return id
    }

    /**
     * 获取ANDROID ID
     */
    @JvmStatic
    fun getAndroidId(context: Context): String {
        return Settings.Secure.getString(context.applicationContext.contentResolver, Settings.Secure.ANDROID_ID)
    }

    /**
     *  获取设备的软件版本号
     */
    @JvmStatic
    @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
    fun getDeviceSoftwareVersion(context: Context): String? {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return tm.deviceSoftwareVersion
    }

    /**
     * 获取手机号
     */
    @JvmStatic
    @RequiresPermission(allOf = [Manifest.permission.READ_SMS, Manifest.permission.READ_PHONE_NUMBERS, Manifest.permission.READ_PHONE_STATE])
    fun getLine1Number(context: Context): String? {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        return tm.line1Number
    }

    /**
     * 获取ISO标准的国家码 cn
     */
    @JvmStatic
    fun getNetworkCountryIso(context: Context): String {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return tm.networkCountryIso
    }

    /**
     *  获取手机类型 MI 6
     */
    @JvmStatic
    fun getPhoneType(context: Context): Int {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return tm.phoneType
    }

    /**
     * 获取服务商名称 中国移动
     */
    @JvmStatic
    fun getSimOperatorName(context: Context): String {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return tm.simOperatorName
    }

    /**
     * 获取设备型号，MI 6
     */
    @JvmStatic
    fun getBuildBrandModel(): String {
        return Build.MODEL
    }

    /**
     * 品牌 Xiaomi
     */
    @JvmStatic
    fun getBuildBrand(): String {
        return Build.BRAND
    }

    /**
     * 获取设备厂商 Xiaomi
     */
    @JvmStatic
    fun getBuildManufacturer(): String {
        return Build.MANUFACTURER// samsung 品牌
    }

    /**
     * 获取sim卡序列号
     *
     */
    @JvmStatic
    @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
    fun getSimSerialNumber(context: Context): String? {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return tm.simSerialNumber
    }

    /**
     * 判断是否手机
     */
    @JvmStatic
    fun isPhone(context: Context): Boolean {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return tm.phoneType != TelephonyManager.PHONE_TYPE_NONE
    }

    /**
     * 获取序列号，有可能为null
     */
    @JvmStatic
    fun getSerialNumber(): String? {
        var serial: String? = null
        try {
            val c = Class.forName("android.os.SystemProperties")
            val get = c.getMethod("get", String::class.java)
            serial = get.invoke(c, "ro.serialno") as String
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return serial
    }
}