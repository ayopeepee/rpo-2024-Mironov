package ru.iu3.fclient.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import ru.iu3.fclient.MainActivity
import ru.iu3.fclient.databinding.ActivityPinpadBinding
import kotlin.experimental.and

class PinpadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPinpadBinding
    private var currentPin: String = EMPTY_STRING
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPinpadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setOnClickListeners()
        shuffleKeys()
    }

    private fun setOnClickListeners() = with(binding) {
        submitButton.setOnClickListener {
            startActivity(Intent(this@PinpadActivity, MainActivity::class.java).apply {
                putExtra("PIN", currentPin)
            })
            finish()
        }
        clearButton.setOnClickListener {
            resetPin()
        }
        key0Button.setOnClickListener { onKeyButtonClicked(it) }
        key1Button.setOnClickListener { onKeyButtonClicked(it) }
        key2Button.setOnClickListener { onKeyButtonClicked(it) }
        key3Button.setOnClickListener { onKeyButtonClicked(it) }
        key4Button.setOnClickListener { onKeyButtonClicked(it) }
        key5Button.setOnClickListener { onKeyButtonClicked(it) }
        key6Button.setOnClickListener { onKeyButtonClicked(it) }
        key7Button.setOnClickListener { onKeyButtonClicked(it) }
        key8Button.setOnClickListener { onKeyButtonClicked(it) }
        key9Button.setOnClickListener { onKeyButtonClicked(it) }
    }

    private fun onKeyButtonClicked(view: View) {
        val key = (view as Button).text.toString()
        if (currentPin.length < 4) {
            currentPin += key
            binding.pinText.text = "*".repeat(currentPin.length)
        }
    }

    private fun resetPin() {
        binding.pinText.text = EMPTY_STRING
        currentPin = EMPTY_STRING
    }

    private fun shuffleKeys() {
        val keys: List<Button> = with(binding) {
            listOf(
                key0Button,
                key1Button,
                key2Button,
                key3Button,
                key4Button,
                key5Button,
                key6Button,
                key7Button,
                key8Button,
                key9Button,
            )
        }

        val keyTexts = keys.map { it.text.toString() }.toMutableList()

        val randomBytes = MainActivity().randomBytes(MAX_KEYS)

        for (i in randomBytes.indices) {
            val keyAt = (randomBytes[i].toInt() and 0xFF) % keys.size
            val temp = keyTexts[i]
            keyTexts[i] = keyTexts[keyAt]
            keyTexts[keyAt] = temp
        }

        keys.forEachIndexed { index, button ->
            button.text = keyTexts[index]
        }
    }

    companion object {
        const val EMPTY_STRING = ""
        const val MAX_KEYS = 10
    }
}