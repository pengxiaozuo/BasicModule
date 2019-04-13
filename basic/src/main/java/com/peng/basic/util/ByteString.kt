/*
 * Copyright (C) 2018 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.peng.basic.util

import android.util.Base64
import java.io.*
import java.nio.charset.Charset

/**
 * okio中的Byte字符串,保留部分功能
 */
class ByteString(val data: ByteArray) : Serializable, Comparable<ByteString> {

    private var hashCode: Int = 0

    private var utf8: String? = null

    fun utf8(): String {
        var result = utf8
        if (result == null) {
            // We don't care if we double-allocate in racy code.
            result = String(data, Charsets.UTF_8)
            utf8 = result
        }
        return result
    }

    fun hex(): String {
        val result = CharArray(data.size * 2)
        var c = 0
        for (b in data) {
            result[c++] = HEX_DIGITS[b shr 4 and 0xf]
            result[c++] = HEX_DIGITS[b and 0xf]
        }
        return String(result)
    }

    fun toAsciiLowercase(): ByteString {
        // Search for an uppercase character. If we don't find one, return this.
        var i = 0
        while (i < data.size) {
            var c = data[i]
            if (c < 'A'.toByte() || c > 'Z'.toByte()) {
                i++
                continue
            }

            // This string is needs to be lowercased. Create and return a new byte string.
            val lowercase = data.copyOf()
            lowercase[i++] = (c - ('A' - 'a')).toByte()
            while (i < lowercase.size) {
                c = lowercase[i]
                if (c < 'A'.toByte() || c > 'Z'.toByte()) {
                    i++
                    continue
                }
                lowercase[i] = (c - ('A' - 'a')).toByte()
                i++
            }
            return ByteString(lowercase)
        }
        return this
    }

    fun toAsciiUppercase(): ByteString {
        // Search for an lowercase character. If we don't find one, return this.
        var i = 0
        while (i < data.size) {
            var c = data[i]
            if (c < 'a'.toByte() || c > 'z'.toByte()) {
                i++
                continue
            }

            // This string is needs to be uppercased. Create and return a new byte string.
            val lowercase = data.copyOf()
            lowercase[i++] = (c - ('a' - 'A')).toByte()
            while (i < lowercase.size) {
                c = lowercase[i]
                if (c < 'a'.toByte() || c > 'z'.toByte()) {
                    i++
                    continue
                }
                lowercase[i] = (c - ('a' - 'A')).toByte()
                i++
            }
            return ByteString(lowercase)
        }
        return this
    }

    fun toByteArray(): ByteArray = data.copyOf()

    fun string(charset: Charset) = String(data, charset)

    fun substring(beginIndex: Int = 0, endIndex: Int = size): ByteString {
        require(beginIndex >= 0) { "beginIndex < 0" }
        require(endIndex <= data.size) { "endIndex > length(${data.size})" }

        val subLen = endIndex - beginIndex
        require(subLen >= 0) { "endIndex < beginIndex" }

        if (beginIndex == 0 && endIndex == data.size) {
            return this
        }

        val copy = ByteArray(subLen)
        System.arraycopy(data, beginIndex, copy, 0, subLen)
        return ByteString(copy)
    }

    fun startsWith(prefix: ByteString): Boolean =
        rangeEquals(0, prefix, 0, prefix.size)

    fun startsWith(prefix: ByteArray): Boolean =
        rangeEquals(0, prefix, 0, prefix.size)


    fun endsWith(suffix: ByteString): Boolean =
        rangeEquals(size - suffix.size, suffix, 0, suffix.size)

    fun endsWith(suffix: ByteArray): Boolean =
        rangeEquals(size - suffix.size, suffix, 0, suffix.size)

    fun indexOf(other: ByteString, fromIndex: Int) = indexOf(other.data, fromIndex)

    fun indexOf(other: ByteArray, fromIndex: Int): Int {
        val limit = data.size - other.size
        for (i in maxOf(fromIndex, 0)..limit) {
            if (arrayRangeEquals(data, i, other, 0, other.size)) {
                return i
            }
        }
        return -1
    }

    fun lastIndexOf(other: ByteString, fromIndex: Int = size) = lastIndexOf(
        other.data, fromIndex
    )

    fun lastIndexOf(other: ByteArray, fromIndex: Int = size): Int {
        val limit = data.size - other.size
        for (i in minOf(fromIndex, limit) downTo 0) {
            if (arrayRangeEquals(data, i, other, 0, other.size)) {
                return i
            }
        }
        return -1
    }

    fun write(out: OutputStream): Unit {
        out.write(data)
    }

    fun rangeEquals(offset: Int, other: ByteString, otherOffset: Int, byteCount: Int) =
        other.rangeEquals(otherOffset, this.data, offset, byteCount)

    fun rangeEquals(
        offset: Int,
        other: ByteArray,
        otherOffset: Int,
        byteCount: Int
    ): Boolean {
        return (offset >= 0 && offset <= data.size - byteCount &&
                otherOffset >= 0 && otherOffset <= other.size - byteCount &&
                arrayRangeEquals(data, offset, other, otherOffset, byteCount))
    }


    @JvmName("getByte")
    operator fun get(index: Int): Byte = data[index]

    val size: Int
        @JvmName("size") get() = data.size


    override fun compareTo(other: ByteString): Int {
        val sizeA = size
        val sizeB = other.size
        var i = 0
        val size = minOf(sizeA, sizeB)
        while (i < size) {
            val byteA = this[i] and 0xff
            val byteB = other[i] and 0xff
            if (byteA == byteB) {
                i++
                continue
            }
            return if (byteA < byteB) -1 else 1
        }
        if (sizeA == sizeB) return 0
        return if (sizeA < sizeB) -1 else 1
    }

    override fun equals(other: Any?): Boolean {
        return when {
            other === this -> true
            other is ByteString -> other.size == data.size && other.rangeEquals(0, data, 0, data.size)
            else -> false
        }
    }

    override fun hashCode(): Int {
        val result = hashCode
        if (result != 0) return result
        hashCode = data.contentHashCode()
        return hashCode
    }

    override fun toString(): String {
        if (data.isEmpty()) return "[size=0]"
        return if (data.size <= 64) {
            "[hex=${hex()}]"
        } else {
            "[size=${data.size} hex=${substring(0, 64).hex()}…]"
        }
    }


    companion object {
        @JvmStatic
        fun of(vararg data: Byte): ByteString = ByteString(data.copyOf())

        @JvmStatic
        fun encodeUtf8(str: String): ByteString {
            val byteString = ByteString(str.toByteArray(Charsets.UTF_8))
            byteString.utf8 = str
            return byteString
        }

        @JvmStatic
        @JvmName("encodeString")
        fun encode(str: String, charset: Charset = Charsets.UTF_8) = ByteString(str.toByteArray(charset))

        @JvmStatic
        @JvmOverloads
        fun decodeBase64(str: String, flags: Int = Base64.NO_WRAP): ByteString? {
            val decoded = Base64.decode(str, flags)
            return if (decoded != null) ByteString(decoded) else null
        }

        @JvmStatic
        fun decodeHex(str: String): ByteString {
            //检查value 如果为false则抛出IllegalArgumentException异常
            require(str.length % 2 == 0) { "Unexpected hex string: $this" }

            val result = ByteArray(str.length / 2)
            for (i in result.indices) {
                val d1 = str[i * 2].decodeHexDigit() shl 4
                val d2 = str[i * 2 + 1].decodeHexDigit()
                result[i] = (d1 + d2).toByte()
            }
            return ByteString(result)
        }

        @Throws(IOException::class)
        @JvmStatic
        @JvmName("read")
        fun readByteString(inputStream: InputStream, byteCount: Int): ByteString {
            require(byteCount >= 0) { "byteCount < 0: $byteCount" }
            val result = ByteArray(byteCount)
            var offset = 0
            var read: Int
            while (offset < byteCount) {
                read = inputStream.read(result, offset, byteCount - offset)
                if (read == -1) throw EOFException()
                offset += read
            }
            return ByteString(result)
        }

        @JvmStatic
        @JvmName("of")
        fun ByteArray.toByteString(offset: Int = 0, byteCount: Int = size): ByteString {
            checkOffsetAndCount(size.toLong(), offset.toLong(), byteCount.toLong())

            val copy = ByteArray(byteCount)
            System.arraycopy(this, offset, copy, 0, byteCount)
            return ByteString(copy)
        }

        private fun checkOffsetAndCount(size: Long, offset: Long, byteCount: Long) {
            if (offset or byteCount < 0 || offset > size || size - offset < byteCount) {
                throw ArrayIndexOutOfBoundsException("size=$size offset=$offset byteCount=$byteCount")
            }
        }
    }
}
