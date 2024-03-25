package ru.iu3.fclient

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import ru.iu3.fclient.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Example of a call to a native method
        binding.sampleText.text = stringFromJNI()
        Log.d("TAG", "onCreate: ${initRng()} --- ${randomBytes(10)}")
    }

    /**
     * A native method that is implemented by the 'fclient' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String
    external fun initRng(): Int
    external fun randomBytes(no: Int): ByteArray

    companion object {
        // Used to load the 'fclient' library on application startup.
        init {
            System.loadLibrary("native-lib")
            System.loadLibrary("mbedcrypto")
        }
    }
}