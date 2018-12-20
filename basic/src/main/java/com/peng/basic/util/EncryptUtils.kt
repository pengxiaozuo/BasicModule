package com.peng.basic.util

import java.math.BigInteger
import java.security.*
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.RSAPrivateKeySpec
import java.security.spec.RSAPublicKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


object EncryptUtils {

    /**
     * DES加密 key 的长度8，NoPadding data的长度8的倍数 如果cbc iv长度8
     */
    @JvmStatic
    @JvmOverloads
    fun encryptDES(
        data: ByteArray, key: ByteArray, transformation: String = "DES/ECB/NoPadding",
        iv: ByteArray? = null
    ): ByteString {
        return desTemplate(data, key, "DES", transformation, true, iv)
    }

    /**
     * DES解密 key 的长度8，NoPadding data的长度8的倍数 如果cbc iv长度8
     */
    @JvmStatic
    @JvmOverloads
    fun decryptDES(
        data: ByteArray, key: ByteArray, transformation: String = "DES/ECB/NoPadding",
        iv: ByteArray? = null
    ): ByteString {
        return desTemplate(data, key, "DES", transformation, false, iv)
    }

    /**
     * AES加密 key 的长度16，NoPadding data的长度16的倍数 如果cbc iv长度16
     */
    @JvmStatic
    @JvmOverloads
    fun encryptAES(
        data: ByteArray, key: ByteArray, transformation: String = "AES/ECB/NoPadding",
        iv: ByteArray? = null
    ): ByteString {
        return desTemplate(data, key, "AES", transformation, true, iv)
    }

    /**
     * AES解密key 的长度16，NoPadding data的长度16的倍数 如果cbc iv长度16
     */
    @JvmStatic
    @JvmOverloads
    fun decryptAES(
        data: ByteArray, key: ByteArray, transformation: String = "AES/ECB/NoPadding",
        iv: ByteArray? = null
    ): ByteString {
        return desTemplate(data, key, "AES", transformation, false, iv)
    }

    /**
     * 3DES加密 key 的长度24，NoPadding data的长度8的倍数 如果cbc iv长度8
     */
    @JvmStatic
    @JvmOverloads
    fun encrypt3DES(
        data: ByteArray, key: ByteArray, transformation: String = "DESede/ECB/NoPadding",
        iv: ByteArray? = null
    ): ByteString {
        return desTemplate(data, key, "DESede", transformation, true, iv)
    }

    /**
     * 3DES解密 key 的长度24，NoPadding data的长度8的倍数 如果cbc iv长度8
     */
    @JvmStatic
    @JvmOverloads
    fun decrypt3DES(
        data: ByteArray, key: ByteArray, transformation: String = "DESede/ECB/NoPadding",
        iv: ByteArray? = null
    ): ByteString {
        return desTemplate(data, key, "DESede", transformation, false, iv)
    }

    @JvmStatic
    @JvmOverloads
    fun desTemplate(
        data: ByteArray, key: ByteArray, algorithm: String, transformation: String,
        isEncrypt: Boolean, iv: ByteArray? = null
    ): ByteString {

        val keySpec = SecretKeySpec(key, algorithm)
        val cipher = Cipher.getInstance(transformation)
        val random = SecureRandom()
        if (transformation.contains("CBC")) {
            cipher.init(if (isEncrypt) Cipher.ENCRYPT_MODE else Cipher.DECRYPT_MODE, keySpec, IvParameterSpec(iv))
        } else {
            cipher.init(if (isEncrypt) Cipher.ENCRYPT_MODE else Cipher.DECRYPT_MODE, keySpec, random)
        }
        return ByteString(cipher.doFinal(data))

    }

    /**
     * 获取RSA私钥
     */
    @JvmStatic
    fun getRSAPrivateKey(key: String, isBase64Encoded: Boolean): PrivateKey {
        val keyByteArray = if (isBase64Encoded) key.toByteArray().base64Decode().toByteArray() else key.toByteArray()
        val keySpec = PKCS8EncodedKeySpec(keyByteArray)
        val keyFactory = KeyFactory.getInstance("RSA")
        return keyFactory.generatePrivate(keySpec)
    }

    /**
     * 获取RSA公钥
     */
    @JvmStatic
    fun getRSAPublicKey(key: String, isBase64Encoded: Boolean): PublicKey {
        val keyByteArray = if (isBase64Encoded) key.toByteArray().base64Decode().toByteArray() else key.toByteArray()
        val keySpec = X509EncodedKeySpec(keyByteArray)
        val keyFactory = KeyFactory.getInstance("RSA")
        return keyFactory.generatePublic(keySpec)
    }


    /**
     * 获取RSA私钥
     */
    @JvmStatic
    fun getRSAPrivateKey(modulus: String, exponent: String): PrivateKey {
        val keySpec = RSAPrivateKeySpec(BigInteger(modulus), BigInteger(exponent))
        val keyFactory = KeyFactory.getInstance("RSA")
        return keyFactory.generatePrivate(keySpec)
    }

    /**
     * 获取RSA公钥
     */
    @JvmStatic
    fun getRSAPublicKey(modulus: String, exponent: String): PublicKey {
        val keySpec = RSAPublicKeySpec(BigInteger(modulus), BigInteger(exponent))
        val keyFactory = KeyFactory.getInstance("RSA")
        return keyFactory.generatePublic(keySpec)
    }

    /**
     * RSA解密
     */
    @JvmStatic
    fun decryptRSA(data: ByteArray, key: Key): ByteString {
        return rsaTemplate(data, key, false)
    }

    /**
     * RSA 加密
     */
    @JvmStatic
    fun encryptRSA(data: ByteArray, key: Key): ByteString {
        return rsaTemplate(data, key, true)
    }

    @JvmStatic
    fun rsaTemplate(data: ByteArray, key: Key, isEncrypt: Boolean): ByteString {

        val cipher = Cipher.getInstance("RSA")
        cipher.init(if (isEncrypt) Cipher.ENCRYPT_MODE else Cipher.DECRYPT_MODE, key)
        return ByteString(cipher.doFinal(data))

    }

}