package com.peng.basic.util

import android.os.Build
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.io.PrintWriter
import java.text.SimpleDateFormat
import java.util.*

object CrashUtils {

    private val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()

    private var dir: File? = null
    private var filename = "-crash.log"
    private var fileDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private var logDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    private val exceptionHandler = Thread.UncaughtExceptionHandler { t, e ->

        if (e == null) {
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0)
        } else {
            if (dir != null) {
                val date = Date()
                val logFile = File(dir, fileDateFormat.format(date) + filename)
                if (!logFile.exists()) {
                    logFile.createNewFile()
                }
                if (logFile.canWrite()) {
                    var pw: PrintWriter? = null
                    try {
                        val crashHead =
                                "\n************* Crash Log Head ****************" +
                                "\nDevice Manufacturer: " + Build.MANUFACTURER +// 设备厂商
                                "\nDevice Model       : " + Build.MODEL +// 设备型号
                                "\nAndroid Version    : " + Build.VERSION.RELEASE +// 系统版本
                                "\nAndroid SDK        : " + Build.VERSION.SDK_INT +// SDK版本
                                "\nTime               : ${logDateFormat.format(date)}" +
                                "\n************* Crash Log Head ****************\n"
                        val filerWriter = FileWriter(logFile, true)
                        pw = PrintWriter(filerWriter)
                        pw.write(crashHead)
                        e.printStackTrace(pw)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    } finally {
                        CloseUtils.close(pw)
                    }
                }
            }
            defaultHandler?.uncaughtException(t, e)
        }
    }

    fun init(crashLogDir: File? = null) {
        dir = crashLogDir
        if (dir?.exists() != true) {
            dir?.mkdirs()
        }

        Thread.setDefaultUncaughtExceptionHandler(exceptionHandler)
    }
}