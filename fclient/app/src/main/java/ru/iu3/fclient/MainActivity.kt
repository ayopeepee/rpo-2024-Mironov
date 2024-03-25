package ru.iu3.fclient

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import org.apache.commons.codec.binary.Hex
import ru.iu3.fclient.databinding.ActivityMainBinding
import ru.iu3.fclient.ui.PinpadActivity
import ru.iu3.fclient.utils.toHex
import ru.iu3.fclient.utils.toast

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("TAG", "onCreate: ${initRng()} --- ${randomBytes(10)}")

        setOnClickListeners()

        intent.getStringExtra("PIN")?.let {
            toast(it)
        }
    }

    private fun setOnClickListeners() = with(binding) {
        clickMeButton.setOnClickListener {
            //toast(encryptThenDecrypt("0123456789ABCDEF0123456789ABCDE0", "000000000000000102"))
            startActivity(Intent(this@MainActivity, PinpadActivity::class.java))
        }
    }

    private fun encryptThenDecrypt(key: String, data: String): String {
        val hexKey = key.toHex()
        val hexData = data.toHex()
        val encrypted = encrypt(hexKey, hexData)
        val decrypted = decrypt(hexKey, encrypted)
        return Hex.encodeHexString(decrypted)
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

    companion object {
        // Used to load the 'fclient' library on application startup.
        init {
            System.loadLibrary("native-lib")
            System.loadLibrary("mbedcrypto")
        }
    }
}