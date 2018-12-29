@file:JvmName("SignUtils")

package com.peng.basic.util

import java.io.File
import java.io.FileInputStream
import java.nio.channels.FileChannel
import java.security.InvalidKeyException
import java.security.MessageDigest
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

/**
 * 获取文件的md5
 */
fun File.md5() = digest("MD5", this)

/**
 * 获取文件的sha1
 */
fun File.sha1() = digest("SHA-1", this)

/**
 * 获取文件的sha256
 */
fun File.sha256() = digest("SHA-256", this)

/**
 * 获取文件的sha512
 */
fun File.sha512() = digest("SHA-512", this)

/**
 * 摘要算法
 *
 * @param algorithm 算法：MD5 SHA-1 SHA-256 SHA-512 等
 */
fun digest(algorithm: String, file: File): ByteString {
    var fis: FileInputStream? = null
    try {
        fis = FileInputStream(file)
        val channel = fis.channel
        val buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, file.length())
        val messageDigest = MessageDigest.getInstance(algorithm)
        messageDigest.update(buffer)
        return ByteString(messageDigest.digest())
    } finally {
        CloseUtils.close(fis)
    }
}

/**
 * 获取数组的md5
 */
fun ByteArray.md5() = digest("MD5", this)

/**
 * 获取数组的sha1
 */
fun ByteArray.sha1() = digest("SHA-1", this)

/**
 * 获取数组的sha256
 */
fun ByteArray.sha256() = digest("SHA-256", this)

/**
 * 获取数组的sha512
 */
fun ByteArray.sha512() = digest("SHA-512", this)

/**
 * 摘要算法
 *
 * @param algorithm 算法：MD5 SHA-1 SHA-256 SHA-512 等
 */
fun digest(algorithm: String, data: ByteArray) =
    ByteString(MessageDigest.getInstance(algorithm).digest(data))

/**
 * 获取数组的hmacmd5
 */
fun ByteArray.hmacMd5(key: ByteArray): ByteString {
    return hmac("HmacMD5", key, this)
}

/**
 * 获取数组的hmaSha1
 */
fun ByteArray.hmacSha1(key: ByteArray): ByteString {
    return hmac("HmacSHA1", key, this)
}

/**
 * 获取数组的hmaSha256
 */
fun ByteArray.hmacSha256(key: ByteArray): ByteString {
    return hmac("HmacSHA256", key, this)
}

/**
 * 获取数组的hmaSha512
 */
fun ByteArray.hmacSha512(key: ByteArray): ByteString {
    return hmac("HmacSHA512", key, this)
}

/**
 * hmac算法
 *
 * @param algorithm 算法：HmacMD5 HmacSHA1 HmacSHA256 HmacSHA512 等
 */
fun hmac(algorithm: String, key: ByteArray, data: ByteArray): ByteString {
    try {
        val mac = Mac.getInstance(algorithm)
        mac.init(SecretKeySpec(key, algorithm))
        return ByteString(mac.doFinal(data))
    } catch (e: InvalidKeyException) {
        throw IllegalArgumentException(e)
    }
}

