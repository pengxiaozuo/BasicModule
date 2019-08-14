package com.peng.basic

import org.junit.Assert.assertEquals
import org.junit.Test
import java.security.KeyPairGenerator
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {

        assertEquals(4, (2 + 2).toLong())
        val pairGenerator = KeyPairGenerator.getInstance("RSA")
        val keyPair = pairGenerator.genKeyPair()
        val publicString = Base64.getEncoder().encodeToString(keyPair.public.encoded)
//        val publicString =keyPair.public.toString()
        val privateString = Base64.getEncoder().encodeToString(keyPair.private.encoded)
        println(publicString)
        println()
        println()
        println(privateString)
//        println(UUID.randomUUID().toString())
//        val signKey = "0c84c5b1-253e-4127-b12c-cc1988376712"
//
//        val timestamp = Date().time / 1000
//        println(timestamp)
//        println()
//        val data = "$timestamp$signKey"
//        val sign = data.toByteArray().md5().hex()
//        println(sign)

    }


}