package com.peng.basic.util

import android.text.TextUtils
import android.util.Log
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


object LogUtils {

    /**
     * 是否打印日志
     */
    var debug = true
    /**
     * 是否缓存到本地
     */
    var diskToCache = false
    /**
     * 是否打印线程信息
     */
    var threadInfo = true
    /**
     * 缓存到本地时每条日志加入的时间格式
     */
    var logDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    /**
     * 缓存到本地时文件名加入的时间格式
     */
    var fileDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    /**
     * 缓存目录
     */
    var cacheDir: File? = null
    /**
     * 缓存级别
     * [Log.DEBUG],[Log.INFO],[Log.WARN],[Log.ERROR]
     */
    var cacheLevel: Int = Log.ERROR
    /**
     * 日志缓存的天数
     */
    var cacheDays = 30
    /**
     * 日志文件的后缀名
     */
    var filename = "-logcat.log"

    @JvmStatic
    @JvmOverloads
    fun i(tag: String, msg: String, throwable: Throwable? = null) {
        println(Log.INFO, tag, msg, throwable)
    }

    @JvmStatic
    @JvmOverloads
    fun i(msg: String, throwable: Throwable? = null) {
        println(Log.INFO, getTag(), msg, throwable)
    }

    @JvmStatic
    @JvmOverloads
    fun d(tag: String, msg: String, throwable: Throwable? = null) {
        println(Log.DEBUG, tag, msg, throwable)
    }

    @JvmStatic
    @JvmOverloads
    fun d(msg: String, throwable: Throwable? = null) {
        println(Log.DEBUG, getTag(), msg, throwable)
    }

    @JvmStatic
    @JvmOverloads
    fun w(tag: String, msg: String, throwable: Throwable? = null) {
        println(Log.WARN, tag, msg, throwable)
    }

    @JvmStatic
    @JvmOverloads
    fun w(msg: String, throwable: Throwable? = null) {
        println(Log.WARN, getTag(), msg, throwable)
    }

    @JvmStatic
    @JvmOverloads
    fun e(tag: String, msg: String, throwable: Throwable? = null) {
        println(Log.ERROR, tag, msg, throwable)
    }

    @JvmStatic
    @JvmOverloads
    fun e(msg: String, throwable: Throwable? = null) {
        println(Log.ERROR, getTag(), msg, throwable)
    }

    @JvmStatic
    @JvmOverloads
    private fun println(level: Int, tag: String, msg: String, throwable: Throwable? = null) {
        val sb = StringBuilder()
        if (debug) {
            if (threadInfo) {
                sb.append("Thread: ${Thread.currentThread().name}: ")
                sb.append(msg)
                if (throwable != null) {
                    sb.append("\n ${Log.getStackTraceString(throwable)}")
                }
            }
            Log.println(level, tag, sb.toString())
        }

        if (diskToCache && level >= cacheLevel) {
            sb.delete(0, sb.length)
            val date = Date()
            sb.append(logDateFormat.format(date) + " ")
            if (threadInfo) {
                sb.append("Thread: ${Thread.currentThread().name} ")
            }
            sb.append(when (level) {
                Log.INFO -> "I/"
                Log.DEBUG -> "D/"
                Log.WARN -> "W/"
                Log.ERROR -> "E/"
                else -> "UNKNOWN/"
            })
            sb.append("$tag: ")

            sb.append(msg)

            if (throwable != null) {
                sb.append("\n ${Log.getStackTraceString(throwable)}")
            }

            writeToDisk(date, sb.toString())
        }
    }

    @JvmStatic
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

    @JvmStatic
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
    @JvmStatic
    private fun getTag(): String {

        val thisClassName = LogUtils::class.java.name
        val stack = Thread.currentThread().stackTrace
        var callName = ""

        var ix = 0
        while (ix < stack.size) {
            val frame = stack[ix]
            val cName = frame.className
            if (cName == thisClassName) {
                break
            }
            ix++
        }
        while (ix < stack.size) {
            val frame = stack[ix]
            val cName = frame.className
            if (cName != thisClassName) {
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