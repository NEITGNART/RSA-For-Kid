package com.example.rsa

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.rsa.databinding.ActivityMainBinding
import java.lang.Exception
import java.math.BigInteger
import kotlinx.coroutines.*

fun DSA.isEmpty() = this.p.toString().isEmpty() || this.q.toString().isEmpty()

fun DSA.isPrime(p: BigInteger) = p.isProbablePrime(100)

class MainActivity : AppCompatActivity() {

    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private lateinit var signMessage: Deferred<String>
    private lateinit var m: List<Int>

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dsa = DSA()
        setContentView(binding.root)

        binding.btnRandom.setOnClickListener {
            dsa.pickRandom()
            // assign the values to the edit text
            binding.edP.setText(dsa.p.toString())
            binding.edQ.setText(dsa.q.toString())
            if (!dsa.isValid()) {
                Toast.makeText(this, "p hoặc q không phải số nguyên tố", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnGenerate.setOnClickListener {


            if (binding.edP.text.toString().isEmpty() || binding.edP.text.toString().isEmpty()) {
                Toast.makeText(this, "Chưa nhập p hoặc q", Toast.LENGTH_SHORT).show()

            } else {
                val p = BigInteger(binding.edP.text.toString())
                val q = BigInteger(binding.edQ.text.toString())

                if (!dsa.isPrime(p) || !dsa.isPrime(q)) {
                    Toast.makeText(this, "p hoặc q không phải số nguyên tố", Toast.LENGTH_SHORT)
                        .show()
                }

                dsa.p = p
                dsa.q = q
                val (e, d) = dsa.generateKey()
                // assign the values to the edit text
                binding.tvPrivateKey.setText(d.toString())
                binding.tvPublicKey.text = e.toString()

            }

        }

        binding.btnSignMessage.setOnClickListener {
            // read the message
            val messageNeedSign = binding.etMessage.text.toString().trim()

            // string has format: "0 1 2 3 4 5 6 7 8 9" like a array

            try {
                // convert the string to array
                m = messageNeedSign.split(" ").map { it.toInt() }.toList()
                signMessage =
                    coroutineScope.async {
                        val signatureList =
                            dsa.signMessage(m, BigInteger(binding.tvPublicKey.text.toString()))
                        val signatureMessage = signatureList.joinToString(" ")
                        signatureMessage
                    }
                Toast.makeText(this, "Ký thành công", Toast.LENGTH_SHORT).show()
                // launch a coroutine to sign the message
            } catch (e: Exception) {
                Toast.makeText(
                    this,
                    "Ký thất bại do văn bản không đúng định dạng",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }

        binding.btnShowResultSignature.setOnClickListener {
            // run await

            // run activity lifecycle

            lifecycleScope.launch {
                val result = signMessage.await()
                binding.tvResultSignature.text = result
            }
        }

        binding.btnVerifySignature.setOnClickListener {
            try {
                val text = binding.etHasSignMessage.text
                // convert text to a list of integer
                val s = text.split(" ").map { it.toInt() }.toList()
                lifecycleScope.launch {
                    val isValid =
                        dsa.verifyMessage(s, BigInteger(binding.tvPrivateKey.text.toString()), m)
                    withContext(Dispatchers.Main) {
//                            Toast.makeText(this, "Văn bản đã được ký", Toast.LENGTH_SHORT)
//                        if (isValid) {
//                        }
                    }
                }

            } catch (e: Exception) {

            }
        }
    }
}
