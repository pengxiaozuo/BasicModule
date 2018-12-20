@file:JvmName("Base64Utils")

package com.peng.basic.util

import android.util.Base64
import java.net.URLDecoder
import java.net.URLEncoder


@JvmOverloads
fun ByteArray.base64Encode(flags: Int = Base64.NO_WRAP) = ByteString(Base64.encode(this, flags))

@JvmOverloads
fun ByteArray.base64Decode(flags: Int = Base64.NO_WRAP) = ByteString(Base64.decode(this, flags))

@JvmOverloads
fun String.urlEncodeUrl(charset: String = "UTF-8") = URLEncoder.encode(this, charset)

@JvmOverloads
fun String.urlDecode(charset: String = "UTF-8") = URLDecoder.decode(this, charset)
