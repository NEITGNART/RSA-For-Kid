package com.example.rsa

import java.math.BigInteger
import java.util.*


class DSA {

    lateinit var p: BigInteger
    lateinit var q: BigInteger
    private lateinit var n: BigInteger
    private val bitLength = 10



    fun pickManual(p: BigInteger, q: BigInteger) {
        this.p = p
        this.q = q
    }

    

    // Check if p and q are prime
    fun isValid(): Boolean = p.isProbablePrime(100) && q.isProbablePrime(100)

    fun pickRandom() {
        val random = Random()
        val p = BigInteger(bitLength, 100, random)
        val q = BigInteger(bitLength, 100, random)
        this.p = p
        this.q = q
    }


    fun generateKey(): Pair<BigInteger, BigInteger> {
        n = p * q
        val phi: BigInteger = (p - BigInteger.ONE) * (q - BigInteger.ONE)
        var e: BigInteger = BigInteger.probablePrime(bitLength, Random())

        while (phi.gcd(e) > BigInteger.ONE && e < phi) e.add(BigInteger.ONE)

        val d = e.modInverse(phi)
        // return public key and private key
        return e to d
    }

    suspend fun signMessage(m: List<Int>, e: BigInteger): List<Int> {
        return m.map { it.toBigInteger().modPow(e, n).toInt() }
    }

    fun verifyMessage(s: List<Int>, d: BigInteger, m: List<Int>): Boolean {
        val veri = s.map { it.toBigInteger().modPow(d, n).toInt() }
        return veri.zip(m).all { it.first == it.second }
    }
}
