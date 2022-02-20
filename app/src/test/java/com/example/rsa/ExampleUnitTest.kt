package com.example.rsa

import org.junit.Test

import org.junit.Assert.*
import java.math.BigInteger
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    fun show() {
        println("Hello world")
    }

    fun generateKey(p: BigInteger, q: BigInteger): Pair<BigInteger, BigInteger> {
        val n: BigInteger = p * q
        val phi: BigInteger = (p - BigInteger.ONE) * (q - BigInteger.ONE)
        var e: BigInteger = BigInteger.probablePrime(512, Random())

        while (phi.gcd(e) > BigInteger.ONE && e < phi)
            e.add(BigInteger.ONE)

        val d = e.modInverse(phi)
        return e to d
    }

    fun signMessage(m: List<Int>, e: BigInteger, n : BigInteger): List<Int> {
        return m.map { it.toBigInteger().modPow(e, n).toInt() }
    }

    fun verifyMessage(s: List<Int>, d: BigInteger, n: BigInteger, m: List<Int>): Boolean {
        val veri = s.map { it.toBigInteger().modPow(d, n).toInt()}
        return veri.zip(m).all {it.first == it.second}
    }

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun generateKey_isCorrect() {
        println(generateKey(p = BigInteger("13"), q = BigInteger("17")))
    }

    @Test
    fun signMessage_isCorrect() {
        val str = "Hello Worldi aljsdfljasldfjladfjasfd"
        val m = str.encodeToByteArray()
        m.forEach {  }
        println(m.decodeToString())

//        val m = listOf(64, 112, 97)
//
//        val s = signMessage(m, BigInteger("7"), BigInteger("221"))
//        s.forEach(::println)
    }

    @Test
    fun verifyMessage_isCorrect() {
        val m = listOf(64, 112, 97)
        val s: List<Int> = signMessage(m, BigInteger("7"), BigInteger("221"))
        val ok = if (verifyMessage(s, d = BigInteger("55"), n = BigInteger("221") , m = m )) "Valid" else "Invalid"
        println(ok)

    }



}