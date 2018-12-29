package com.peng.basic.util

import java.io.Closeable
import java.io.IOException

object CloseUtils {
    /**
     * 关闭IO通道
     */
    @JvmStatic
    fun close(vararg closeables: Closeable?) {
        for (closeable in closeables) {
            try {
                closeable?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 静默关闭IO通道
     */
    @JvmStatic
    fun closeQuietly(vararg closeables: Closeable?) {
        for (closeable in closeables) {
            try {
                closeable?.close()
            } catch (e: IOException) {
            }
        }
    }
}