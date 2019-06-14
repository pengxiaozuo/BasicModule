package com.peng.basic.util

import android.text.TextUtils
import android.util.Log
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * 日志工具类
 */
object LogUtils {

    /**
     * 是否打印日志
     */
    @JvmField
    var debug = true
    /**
     * 是否缓存到本地
     */
    @JvmField
    var cacheToDisk = false
    /**
     * 是否打印线程信息
     */
    @JvmField
    var showThreadName = true
    /**
     * 缓存到本地时每条日志加入的时间格式
     */
    @JvmField
    var logDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    /**
     * 缓存到本地时文件名加入的时间格式
     */
    @JvmField
    var fileDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    /**
     * 缓存目录
     */
    @JvmField
    var cacheDir: File? = null
    /**
     * 缓存级别
     * [Log.DEBUG],[Log.INFO],[Log.WARN],[Log.ERROR]
     */
    @JvmField
    var cacheLevel: Int = Log.ERROR
    /**
     * 日志缓存的天数
     */
    @JvmField
    var cacheDays = 30
    /**
     * 日志文件的后缀名
     */
    @JvmField
    var filename = "-logcat.log"

    private val executorService = ThreadPoolExecutor(0, 1, 60, TimeUnit.SECONDS,
        LinkedBlockingQueue<Runnable>(), ThreadFactory {
            val result = Thread(it, "LogUtils-Write-Thread")
            result.isDaemon = true
            return@ThreadFactory result
        })

    @JvmStatic
    @JvmOverloads
    fun i(
        tag: String, msg: Any?, throwable: Throwable? = null,
        showThreadName: Boolean = this.showThreadName,
        cacheToDisk: Boolean = this.cacheToDisk,
        cacheLevel: Int = this.cacheLevel,
        dateFormat: DateFormat = this.logDateFormat
    ) {
        println(Log.INFO, tag, msg, throwable, showThreadName, cacheToDisk, cacheLevel, dateFormat)
    }

    @JvmStatic
    @JvmOverloads
    fun i(
        msg: Any?, throwable: Throwable? = null,
        showThreadName: Boolean = this.showThreadName,
        cacheToDisk: Boolean = this.cacheToDisk,
        cacheLevel: Int = this.cacheLevel,
        dateFormat: DateFormat = this.logDateFormat
    ) {
        i(getTag(), msg, throwable, showThreadName, cacheToDisk, cacheLevel, dateFormat)
    }

    @JvmStatic
    @JvmOverloads
    fun d(
        tag: String, msg: Any?, throwable: Throwable? = null,
        showThreadName: Boolean = this.showThreadName,
        cacheToDisk: Boolean = this.cacheToDisk,
        cacheLevel: Int = this.cacheLevel,
        dateFormat: DateFormat = this.logDateFormat
    ) {
        println(Log.DEBUG, tag, msg, throwable, showThreadName, cacheToDisk, cacheLevel, dateFormat)
    }

    @JvmStatic
    @JvmOverloads
    fun d(
        msg: Any?, throwable: Throwable? = null,
        showThreadName: Boolean = this.showThreadName,
        cacheToDisk: Boolean = this.cacheToDisk,
        cacheLevel: Int = this.cacheLevel,
        dateFormat: DateFormat = this.logDateFormat
    ) {
        d(getTag(), msg, throwable, showThreadName, cacheToDisk, cacheLevel, dateFormat)
    }

    @JvmStatic
    @JvmOverloads
    fun w(
        tag: String, msg: Any?, throwable: Throwable? = null,
        showThreadName: Boolean = this.showThreadName,
        cacheToDisk: Boolean = this.cacheToDisk,
        cacheLevel: Int = this.cacheLevel,
        dateFormat: DateFormat = this.logDateFormat
    ) {
        println(Log.WARN, tag, msg, throwable, showThreadName, cacheToDisk, cacheLevel, dateFormat)
    }

    @JvmStatic
    @JvmOverloads
    fun w(
        msg: Any?, throwable: Throwable? = null,
        showThreadName: Boolean = this.showThreadName,
        cacheToDisk: Boolean = this.cacheToDisk,
        cacheLevel: Int = this.cacheLevel,
        dateFormat: DateFormat = this.logDateFormat
    ) {
        w(getTag(), msg, throwable, showThreadName, cacheToDisk, cacheLevel, dateFormat)
    }

    @JvmStatic
    @JvmOverloads
    fun e(
        tag: String, msg: Any?, throwable: Throwable? = null,
        showThreadName: Boolean = this.showThreadName,
        cacheToDisk: Boolean = this.cacheToDisk,
        cacheLevel: Int = this.cacheLevel,
        dateFormat: DateFormat = this.logDateFormat
    ) {
        println(Log.ERROR, tag, msg, throwable, showThreadName, cacheToDisk, cacheLevel, dateFormat)
    }

    @JvmStatic
    @JvmOverloads
    fun e(
        msg: Any?, throwable: Throwable? = null,
        showThreadName: Boolean = this.showThreadName,
        cacheToDisk: Boolean = this.cacheToDisk,
        cacheLevel: Int = this.cacheLevel,
        dateFormat: DateFormat = this.logDateFormat
    ) {
        e(getTag(), msg, throwable, showThreadName, cacheToDisk, cacheLevel, dateFormat)
    }

    private fun println(
        level: Int,
        tag: String,
        msg: Any?,
        throwable: Throwable? = null,
        showThreadName: Boolean = this.showThreadName,
        cacheToDisk: Boolean = this.cacheToDisk,
        cacheLevel: Int = this.cacheLevel,
        dateFormat: DateFormat = this.logDateFormat
    ) {
        val sb = StringBuilder()
        if (debug) {
            if (showThreadName) {
                sb.append("Thread:[${Thread.currentThread().name}] ")
                sb.append(msg)
                if (throwable != null) {
                    sb.append("\n ${Log.getStackTraceString(throwable)}")
                }
            }
            formatPrint(level, tag, sb.toString())
        }

        if (cacheToDisk && level >= cacheLevel) {
            sb.delete(0, sb.length)
            val date = Date()
            sb.append(dateFormat.format(date) + " ")
            if (showThreadName) {
                sb.append("Thread: ${Thread.currentThread().name} ")
            }
            sb.append(
                when (level) {
                    Log.INFO -> "I/"
                    Log.DEBUG -> "D/"
                    Log.WARN -> "W/"
                    Log.ERROR -> "E/"
                    else -> "UNKNOWN/"
                }
            )
            sb.append("$tag: ")

            sb.append(msg)

            if (throwable != null) {
                sb.append("\n ${Log.getStackTraceString(throwable)}")
            }

            executorService.submit {
                writeToDisk(date, sb.toString())
            }
        }
    }

    private const val TOP_LEFT_CORNER = "╔"
    private const val BOTTOM_LEFT_CORNER = "╚"
    private const val LEFT_CORNER = "║"
    private const val LINE = "══════════════════════════════════════════"

    private fun formatPrint(level: Int, tag: String, msg: String) {
        val lines = msg.split(System.getProperty("line.separator"))
        if (lines.size > 1) {
            Log.println(level, tag, "$TOP_LEFT_CORNER$LINE$LINE")
            lines.forEach { s ->
                Log.println(level, tag, "$LEFT_CORNER $s")
            }
            Log.println(level, tag, "$BOTTOM_LEFT_CORNER$LINE$LINE")
        } else {
            Log.println(level, tag, msg)
        }
    }

    private fun writeToDisk(date: Date, msg: String) {
        cacheDir?.let {

            if (!it.exists()) {
                try {
                    it.mkdirs()
                } catch (e: Exception) {
                    e.printStackTrace()
                    return
                }
            }

            if (!it.isDirectory) return

            val logFile = File(cacheDir, fileDateFormat.format(date) + filename)
            if (!logFile.exists()) {
                try {
                    logFile.createNewFile()
                } catch (e: Exception) {
                    e.printStackTrace()
                    return
                }
                deleteFile(date)
            }
            if (logFile.canWrite()) {
                try {
                    val filerWriter = FileWriter(logFile, true)
                    val bufWriter = BufferedWriter(filerWriter)
                    bufWriter.write(msg)
                    bufWriter.newLine()
                    bufWriter.close()
                    filerWriter.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun deleteFile(date: Date) {
        cacheDir?.let {
            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - cacheDays)
            val time = calendar.timeInMillis

            val files = it.listFiles()
            for (file in files) {
                if (file.name.length < "${fileDateFormat.format(date)}$filename".length) {
                    file.delete()
                }
                try {
                    val oldFileDate = file.name.substring(0, fileDateFormat.format(date).length)
                    println(oldFileDate)
                    val oldTime = fileDateFormat.parse(oldFileDate).time
                    if (oldTime < time) {
                        file.delete()
                    }
                } catch (e: Exception) {

                    file.delete()
                }
            }

        }
    }


    /**
     * 获取非本类的调用者的简单类名
     *
     * @return
     */
    private fun getTag(): String {

        val thisClassName = LogUtils::class.java.name
        val thisKTClassName = thisClassName + "Kt"
        val stack = Thread.currentThread().stackTrace
        var callName = ""

        var ix = 0
        while (ix < stack.size) {
            val frame = stack[ix]
            val cName = frame.className
            if (cName == thisClassName || cName == thisKTClassName) {
                break
            }
            ix++
        }
        while (ix < stack.size) {
            val frame = stack[ix]
            val cName = frame.className
            if (cName != thisClassName && cName != thisKTClassName) {
                try {
                    callName = Class.forName(cName).simpleName
                    if (TextUtils.isEmpty(callName)) {
                        if (!TextUtils.isEmpty(cName)) {
                            callName = if (cName.contains(".") && cName.length > 1) {
                                cName.substring(cName.lastIndexOf(".") + 1)
                            } else {
                                cName
                            }
                        }
                    }
                } catch (e: ClassNotFoundException) {
                    e.printStackTrace()
                    if (!TextUtils.isEmpty(cName)) {
                        callName = if (cName.contains(".") && cName.length > 1) {
                            cName.substring(cName.lastIndexOf(".") + 1)
                        } else {
                            cName
                        }
                    }
                }
                break
            }
            ix++
        }
        return callName
    }
}

fun logi(
    msg: Any?, tag: String? = null, throwable: Throwable? = null,
    showThreadName: Boolean? = null,
    cacheToDisk: Boolean? = null,
    cacheLevel: Int? = null,
    dateFormat: DateFormat? = null
) {
    if (tag != null) {
        LogUtils.i(
            tag, msg, throwable,
            showThreadName ?: LogUtils.showThreadName,
            cacheToDisk ?: LogUtils.cacheToDisk,
            cacheLevel ?: LogUtils.cacheLevel,
            dateFormat ?: LogUtils.logDateFormat
        )
    } else {
        LogUtils.i(
            msg, throwable,
            showThreadName ?: LogUtils.showThreadName,
            cacheToDisk ?: LogUtils.cacheToDisk,
            cacheLevel ?: LogUtils.cacheLevel,
            dateFormat ?: LogUtils.logDateFormat
        )
    }
}

fun logd(
    msg: Any?, tag: String? = null, throwable: Throwable? = null,
    showThreadName: Boolean? = null,
    cacheToDisk: Boolean? = null,
    cacheLevel: Int? = null,
    dateFormat: DateFormat? = null
) {
    if (tag != null) {
        LogUtils.d(
            tag, msg, throwable,
            showThreadName ?: LogUtils.showThreadName,
            cacheToDisk ?: LogUtils.cacheToDisk,
            cacheLevel ?: LogUtils.cacheLevel,
            dateFormat ?: LogUtils.logDateFormat
        )
    } else {
        LogUtils.d(
            msg, throwable,
            showThreadName ?: LogUtils.showThreadName,
            cacheToDisk ?: LogUtils.cacheToDisk,
            cacheLevel ?: LogUtils.cacheLevel,
            dateFormat ?: LogUtils.logDateFormat
        )
    }
}

fun logw(
    msg: Any?, tag: String? = null, throwable: Throwable? = null,
    showThreadName: Boolean? = null,
    cacheToDisk: Boolean? = null,
    cacheLevel: Int? = null,
    dateFormat: DateFormat? = null
) {
    if (tag != null) {
        LogUtils.w(
            tag, msg, throwable,
            showThreadName ?: LogUtils.showThreadName,
            cacheToDisk ?: LogUtils.cacheToDisk,
            cacheLevel ?: LogUtils.cacheLevel,
            dateFormat ?: LogUtils.logDateFormat
        )
    } else {
        LogUtils.w(
            msg, throwable,
            showThreadName ?: LogUtils.showThreadName,
            cacheToDisk ?: LogUtils.cacheToDisk,
            cacheLevel ?: LogUtils.cacheLevel,
            dateFormat ?: LogUtils.logDateFormat
        )
    }
}

fun loge(
    msg: Any?, tag: String? = null, throwable: Throwable? = null,
    showThreadName: Boolean? = null,
    cacheToDisk: Boolean? = null,
    cacheLevel: Int? = null,
    dateFormat: DateFormat? = null
) {
    if (tag != null) {
        LogUtils.e(
            tag, msg, throwable,
            showThreadName ?: LogUtils.showThreadName,
            cacheToDisk ?: LogUtils.cacheToDisk,
            cacheLevel ?: LogUtils.cacheLevel,
            dateFormat ?: LogUtils.logDateFormat
        )
    } else {
        LogUtils.e(
            msg, throwable,
            showThreadName ?: LogUtils.showThreadName,
            cacheToDisk ?: LogUtils.cacheToDisk,
            cacheLevel ?: LogUtils.cacheLevel,
            dateFormat ?: LogUtils.logDateFormat
        )
    }
}
