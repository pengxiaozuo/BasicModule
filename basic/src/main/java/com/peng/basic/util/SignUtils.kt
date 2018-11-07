package com.peng.basic.util

import java.io.File
import java.io.FileInputStream
import java.nio.channels.FileChannel
import java.security.MessageDigest


object SignUtils {

    /**
     * MD5签名
     */
    @JvmStatic
    fun md5ToString(data: String): String? {
        val byteArray = md5(data)
        return if (byteArray == null)
            null
        else
            HexUtils.byteArrayToHexString(byteArray)
    }

    /**
     * MD5签名
     */
    @JvmStatic
    fun md5ToString(data: ByteArray): String? {
        val byteArray = md5(data)
        return if (byteArray == null)
            null
        else
            HexUtils.byteArrayToHexString(byteArray)
    }

    /**
     * MD5签名
     */
    @JvmStatic
    fun md5ToString(file: File): String? {
        val byteArray = md5(file)
        return if (byteArray == null)
            null
        else
            HexUtils.byteArrayToHexString(byteArray)
    }

    /**
     * MD5签名
     */
    @JvmStatic
    fun md5(data: String): ByteArray? {
        return md5(data.toByteArray())
    }

    /**
     * MD5签名
     */
    @JvmStatic
    fun md5(data: ByteArray): ByteArray? {

        var algorithm: MessageDigest? = null
        try {
            algorithm = MessageDigest.getInstance("MD5")
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

        return algorithm!!.digest(data)
    }

    /**
     * MD5签名
     */
    @JvmStatic
    fun md5(file: File): ByteArray? {
        var algorithm: MessageDigest? = null
        try {
            algorithm = MessageDigest.getInstance("MD5")
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
        var fis: FileInputStream? = null
        try {
            fis = FileInputStream(file)
            val channel = fis.channel
            val buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, file.length())
            algorithm.update(buffer)
            return algorithm.digest()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            CloseUtils.close(fis)
        }
        return null
    }


    //============SHA1================
    /**
     * SHA1签名
     */
    @JvmStatic
    fun sha1ToString(data: String): String? {
        val byteArray = sha1(data)
        return if (byteArray == null)
            null
        else
            HexUtils.byteArrayToHexString(byteArray)
    }

    /**
     * SHA1签名
     */
    @JvmStatic
    fun sha1ToString(data: ByteArray): String? {
        val byteArray = sha1(data)
        return if (byteArray == null)
            null
        else
            HexUtils.byteArrayToHexString(byteArray)
    }

    /**
     * SHA1签名
     */
    @JvmStatic
    fun sha1ToString(file: File): String? {
        val byteArray = sha1(file)
        return if (byteArray == null)
            null
        else
            HexUtils.byteArrayToHexString(byteArray)
    }

    /**
     * SHA1签名
     */
    @JvmStatic
    fun sha1(data: String): ByteArray? {
        return sha1(data.toByteArray())
    }

    /**
     * SHA1签名
     */
    @JvmStatic
    fun sha1(data: ByteArray): ByteArray? {

        var algorithm: MessageDigest? = null
        try {
            algorithm = MessageDigest.getInstance("SHA-1")
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

        return algorithm!!.digest(data)
    }

    /**
     * SHA1签名
     */
    @JvmStatic
    fun sha1(file: File): ByteArray? {
        var algorithm: MessageDigest? = null
        try {
            algorithm = MessageDigest.getInstance("SHA-1")
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
        var fis: FileInputStream? = null
        try {
            fis = FileInputStream(file)
            val channel = fis.channel
            val buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, file.length())
            algorithm.update(buffer)
            return algorithm.digest()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            CloseUtils.close(fis)
        }
        return null
    }

    //===============SHA256======================
    /**
     * SHA256签名
     */
    @JvmStatic
    fun sha256ToString(data: String): String? {
        val byteArray = sha256(data)
        return if (byteArray == null)
            null
        else
            HexUtils.byteArrayToHexString(byteArray)
    }

    /**
     * SHA256签名
     */
    @JvmStatic
    fun sha256ToString(data: ByteArray): String? {
        val byteArray = sha256(data)
        return if (byteArray == null)
            null
        else
            HexUtils.byteArrayToHexString(byteArray)
    }

    /**
     * SHA256签名
     */
    @JvmStatic
    fun sha256ToString(file: File): String? {
        val byteArray = sha256(file)
        return if (byteArray == null)
            null
        else
            HexUtils.byteArrayToHexString(byteArray)
    }

    /**
     * SHA256签名
     */
    @JvmStatic
    fun sha256(data: String): ByteArray? {
        return sha256(data.toByteArray())
    }

    /**
     * SHA256签名
     */
    @JvmStatic
    fun sha256(data: ByteArray): ByteArray? {

        var algorithm: MessageDigest? = null
        try {
            algorithm = MessageDigest.getInstance("SHA-256")
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

        return algorithm!!.digest(data)
    }

    /**
     * SHA256签名
     */
    @JvmStatic
    fun sha256(file: File): ByteArray? {
        var algorithm: MessageDigest? = null
        try {
            algorithm = MessageDigest.getInstance("SHA-256")
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
        var fis: FileInputStream? = null
        try {
            fis = FileInputStream(file)
            val channel = fis.channel
            val buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, file.length())
            algorithm.update(buffer)
            return algorithm.digest()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            CloseUtils.close(fis)
        }
        return null
    }


}