package com.peng.basic.util

import java.io.Closeable
import java.io.IOException

object CloseUtils {
    @JvmStatic
    fun close(vararg closeables: Closeable?) {
        for (closeable in closeables) {
            try {
                closeable?.close()
            } catch (e: IOException) {

            }
        }
    }
}