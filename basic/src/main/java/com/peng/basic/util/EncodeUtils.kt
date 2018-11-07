package com.peng.basic.util

import android.util.Base64
import java.net.URLDecoder
import java.net.URLEncoder


object EncodeUtils {

    /**
     * URL编码
     */
    @JvmStatic
    fun urlEncode(url: String, charset: String = "UTF-8"): String {
        return URLEncoder.encode(url, charset)
    }

    /**
     * URL解码
     */
    @JvmStatic
    fun urlDecode(url: String, charset: String = "UTF-8"): String {
        return URLDecoder.decode(url, charset)
    }

    /**
     * Base64编码
     */
    @JvmStatic
    fun base64Encode2String(data: String): String {
        return Base64.encodeToString(data.toByteArray(), Base64.NO_WRAP)
    }

    /**
     * Base64编码
     */
    @JvmStatic
    fun base64Encode2String(data: ByteArray): String {
        return Base64.encodeToString(data, Base64.NO_WRAP)
    }

    /**
     * Base64编码
     */
    @JvmStatic
    fun base64Encode(data: String): ByteArray {
        return Base64.encode(data.toByteArray(), Base64.NO_WRAP)
    }

    /**
     * Base64编码
     */
    @JvmStatic
    fun base64Encode(data: ByteArray): ByteArray {
        return Base64.encode(data, Base64.NO_WRAP)
    }

    /**
     * Base64解码
     */
    @JvmStatic
    fun base64Decode2String(data: ByteArray): String {
        return String(Base64.decode(data, Base64.NO_WRAP))
    }

    /**
     * Base64解码
     */
    @JvmStatic
    fun base64Decode2String(data: String): String {
        return String(Base64.decode(data, Base64.NO_WRAP))
    }

    /**
     * Base64解码
     */
    @JvmStatic
    fun base64Decode(data: ByteArray): ByteArray {
        return Base64.decode(data, Base64.NO_WRAP)
    }

    /**
     * Base64解码
     */
    @JvmStatic
    fun base64Decode(data: String): ByteArray {
        return Base64.decode(data, Base64.NO_WRAP)
    }

}