@file:JvmName("Util")

package com.peng.basic.util

@JvmField
val HEX_DIGITS =
    charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')

@Suppress("NOTHING_TO_INLINE")
inline infix fun Byte.shr(other: Int): Int = toInt() shr other

@Suppress("NOTHING_TO_INLINE")
inline infix fun Byte.shl(other: Int): Int = toInt() shl other

@Suppress("NOTHING_TO_INLINE")
inline infix fun Byte.and(other: Int): Int = toInt() and other

@Suppress("NOTHING_TO_INLINE")
inline infix fun Byte.and(other: Long): Long = toLong() and other

@Suppress("NOTHING_TO_INLINE")
inline infix fun Int.and(other: Long): Long = toLong() and other

@Suppress("NOTHING_TO_INLINE")
inline fun minOf(a: Long, b: Int): Long = minOf(a, b.toLong())

@Suppress("NOTHING_TO_INLINE")
inline fun minOf(a: Int, b: Long): Long = minOf(a.toLong(), b)

fun arrayRangeEquals(
    a: ByteArray,
    aOffset: Int,
    b: ByteArray,
    bOffset: Int,
    byteCount: Int
): Boolean {
    for (i in 0 until byteCount) {
        if (a[i + aOffset] != b[i + bOffset]) return false
    }
    return true
}

/**
 * 转化为16进制字符串
 */
@Suppress("NOTHING_TO_INLINE")
inline fun Long.toHexString(): String {
    return String.format("%016x", this).toUpperCase()
}

/**
 * 转化为16进制字符串
 */
@Suppress("NOTHING_TO_INLINE")
inline fun Int.toHexString(): String {
    return String.format("%08x", this).toUpperCase()
}

/**
 * 转化为16进制字符串
 */
@Suppress("NOTHING_TO_INLINE")
inline fun Short.toHexString(): String {
    return String.format("%04x", this).toUpperCase()
}

/**
 * 转化为16进制字符串
 */
@Suppress("NOTHING_TO_INLINE")
inline fun Byte.toHexString(): String {
    return String.format("%02x", this).toUpperCase()
}

/**
 * 将一个16进制的字符转化为对应的int值 如 'E' -> 14
 */
@Suppress("NOTHING_TO_INLINE")
inline fun Char.decodeHexDigit(): Int {
    val c = this

    return when (c) {
        in '0'..'9' -> c - '0'
        in 'a'..'f' -> c - 'a' + 10
        in 'A'..'F' -> c - 'A' + 10
        else -> throw IllegalArgumentException("Unexpected hex digit: $c")
    }
}

/**
 * 转化为16进制字符串
 */
fun ByteArray.toHexString(): String {
    val result = CharArray(this.size * 2)
    var c = 0
    for (b in this) {
        result[c++] = HEX_DIGITS[b shr 4 and 0xf]
        result[c++] = HEX_DIGITS[b and 0xf]
    }
    return String(result).toUpperCase()
}

/**
 * hex字符串转化为ByteArray
 */
fun String.decodeHex(): ByteArray {
    require(length % 2 == 0) { "Unexpected hex string: $this" }

    val result = ByteArray(length / 2)
    for (i in result.indices) {
        val d1 = this[i * 2].decodeHexDigit() shl 4
        val d2 = this[i * 2 + 1].decodeHexDigit()
        result[i] = (d1 + d2).toByte()
    }
    return result
}
