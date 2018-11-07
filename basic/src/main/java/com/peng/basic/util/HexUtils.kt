package com.peng.basic.util

import java.lang.IllegalArgumentException
import java.util.*

/**
 * Hex ByteArray Int Byte 转换类
 */
object HexUtils {

    /**
     * 将Int值转换为ByteArray
     */
    @JvmStatic
    fun intToByteArray(i: Int): ByteArray {
        return hexStringToByteArray(intToHexString(i))
    }

    /**
     * 将Int值转换为十六进制字符串
     * @param outLength 输出字符串的长度
     */
    @JvmStatic
    fun intToHexString(i: Int, outLength: Int = 8, upperCase: Boolean = true): String {
        var str = Integer.toHexString(i)
        if (str.length < outLength) {
            for (j in str.length until outLength) {
                str = "0$str"
            }
        }
        return if (upperCase) str.toUpperCase() else str.toLowerCase()
    }

    /**
     * 将ByteArray转换为Int
     *
     * 如果ByteArray的长度大于4键抛出异常
     */
    @JvmStatic
    fun byteArrayToInt(bytes: ByteArray): Int {
        if (bytes.size > 4) {
            throw IllegalArgumentException("byte array size must <= 4")
        }

        return hexStringToInt(byteArrayToHexString(bytes))
    }

    /**
     * 将ByteArray转换为十六进制字符串
     */
    @JvmStatic
    fun byteArrayToHexString(bytes: ByteArray, upperCase: Boolean = true): String {

        var str = ""
        for (b in bytes) {
            str += byteToHexString(b)
        }
        return if (upperCase) str.toUpperCase() else str.toLowerCase()
    }

    /**
     * 将Byte转换为十六进制字符串
     */
    @JvmStatic
    fun byteToHexString(b: Byte) = (Integer.toHexString(b.toInt() or 0x100).takeLast(2)).toUpperCase()

    /**
     * 将十六进制字符串转换为Byte
     *
     * 如果字符串长度不等于2将抛出异常
     */
    @JvmStatic
    fun hexStringToByte(hexString: String): Byte {
        if (hexString.length != 2) {
            throw IllegalArgumentException("hexString.length must = 2")
        }
        val i = Character.digit(hexString[1], 16) + (Character.digit(hexString[0], 16) shl 4)
        return i.toByte()
    }

    /**
     * 将十六进制的字符串转换为Int值
     */
    @JvmStatic
    fun hexStringToInt(hexString: String): Int {
        return Integer.parseInt(hexString, 16)
    }

    /**
     * 十六进制字符串转换成ByteArray
     */
    @JvmStatic
    fun hexStringToByteArray(hexString: String): ByteArray {
        if (hexString.length % 2 != 0) {
            throw IllegalArgumentException("hexString.length % 2 != 0")
        }
        val size = hexString.length / 2
        val bytes = ByteArray(size)
        if (bytes.isNotEmpty()) {
            for (i in 0 until bytes.size) {
                bytes[i] = hexStringToByte(hexString.substring(i * 2, i * 2 + 2))
            }
        }
        return bytes
    }

    /**
     * 将多个ByteArray按照顺序合并成一个ByteArray
     */
    @JvmStatic
    fun mergeByteArray(vararg elements: ByteArray): ByteArray {

        val list = ArrayList<Byte>()
        list.clear()
        for (element in elements) {
            list.addAll(element.toTypedArray())
        }
        return list.toByteArray()
    }

    /**
     * 分包
     * @param data 原数据
     * @param partLength 每个包的大小 <1 不做分包
     * @param count 限制分包数量 -- 0不限制 ,[count] 大于  可分包数量 返回实际分包结果 最多可返回[count]+1个结果
     */
    @JvmStatic
    fun partsByteArray(data: ByteArray, partLength: Int, count: Int = 0): List<ByteArray> {

        val list = ArrayList<ByteArray>()
        if (partLength < 1) {
            list.add(data)
            return list
        }
        var maxLength = partLength * count
        if (maxLength == 0 || maxLength > data.size) {
            maxLength = data.size
        }

        list.clear()
        for (i in 0 until maxLength step partLength) {
            var toIndex = i + partLength
            if (toIndex > data.size) {
                toIndex = data.size
            }
            list.add(data.copyOfRange(i, toIndex))
        }

        if (maxLength != data.size) {
            list.add(data.copyOfRange(maxLength, data.size))
        }
        return list
    }
}