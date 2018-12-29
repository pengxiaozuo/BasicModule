@file:JvmName("Base64Utils")

package com.peng.basic.util

import android.util.Base64
import java.net.URLDecoder
import java.net.URLEncoder

/**
 * Base64编码
 */
@JvmOverloads
fun ByteArray.base64Encode(flags: Int = Base64.NO_WRAP) = ByteString(Base64.encode(this, flags))

/**
 * Base64解码
 */
@JvmOverloads
fun ByteArray.base64Decode(flags: Int = Base64.NO_WRAP) = ByteString(Base64.decode(this, flags))

/**
 * Url编码
 */
@JvmOverloads
fun String.urlEncodeUrl(charset: String = "UTF-8") = URLEncoder.encode(this, charset)


/**
 * Url解码
 */
@JvmOverloads
fun String.urlDecode(charset: String = "UTF-8") = URLDecoder.decode(this, charset)
