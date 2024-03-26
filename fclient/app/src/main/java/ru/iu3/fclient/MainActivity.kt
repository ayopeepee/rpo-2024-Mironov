package ru.iu3.fclient

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import org.apache.commons.codec.binary.Hex
import ru.iu3.fclient.databinding.ActivityMainBinding
import ru.iu3.fclient.ui.PinpadActivity
import ru.iu3.fclient.utils.TransactionEvents
import ru.iu3.fclient.utils.toHex
import ru.iu3.fclient.utils.toast
import java.util.Objects

class MainActivity : AppCompatActivity(), TransactionEvents {

    private lateinit var binding: ActivityMainBinding
    private lateinit var activityForResult: ActivityResultLauncher<Intent>
    private var currentPin: String = EMPTY_STRING

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("TAG", "onCreate: ${initRng()} --- ${randomBytes(10)}")

        setOnClickListeners()

        activityForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.getStringExtra("PIN")?.let {
                    currentPin = it
                    synchronized(this) {
                        (this as Object).notifyAll()
                    }
                }
            }
        }
    }

    private fun setOnClickListeners() = with(binding) {
        clickMeButton.setOnClickListener {
            //toast(encryptThenDecrypt("0123456789ABCDEF0123456789ABCDE0", "000000000000000102"))
            val data = "9F0206000000000100".toHex()
            transaction(data)
        }
    }

    private fun encryptThenDecrypt(key: String, data: String): String {
        val hexKey = key.toHex()
        val hexData = data.toHex()
        val encrypted = encrypt(hexKey, hexData)
        val decrypted = decrypt(hexKey, encrypted)
        return Hex.encodeHexString(decrypted)
    }

    override fun enterPin(attempts: Int, amount: String): String {
        val intent = Intent(this, PinpadActivity::class.java).apply {
            putExtra("ARG_ATTEMPTS", attempts)
            putExtra("ARG_AMOUNT", amount)
        }

        synchronized(this) {
            activityForResult.launch(intent)
            try {
                (this as Object).wait()
            } catch (e: Exception) {
                Log.e("TAG", "enterPin:", e)
            }
        }
        return currentPin
    }

    override fun transactionResult(result: Boolean) {
        runOnUiThread {
            toast(if (result) "ok" else "failed")
        }
    }

    /**
     * A native method that is implemented by the 'fclient' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String
    external fun initRng(): Int
    external fun randomBytes(no: Int): ByteArray

    external fun encrypt(key: ByteArray, data: ByteArray): ByteArray
    external fun decrypt(key: ByteArray, data: ByteArray): ByteArray

    external fun transaction(trd: ByteArray): Boolean

    companion object {
        // Used to load the 'fclient' library on application startup.
        init {
            System.loadLibrary("native-lib")
            System.loadLibrary("mbedcrypto")
        }

        const val EMPTY_STRING = ""
    }
}