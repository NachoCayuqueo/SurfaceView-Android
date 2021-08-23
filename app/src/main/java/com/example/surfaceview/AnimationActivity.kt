package com.example.surfaceview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class AnimationActivity : AppCompatActivity() {

    private var buttonState: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animation)
        supportActionBar?.hide()

        val username = intent.getStringExtra("username")
        val textView = findViewById<TextView>(R.id.textViewID)
        textView.text = username

        buttonsAction()
    }

    private fun buttonsAction(){
        buttonPause()
        buttonGoBack()
    }

    private fun buttonPause() {
        val buttonPause = findViewById<Button>(R.id.buttonPauseID)
        buttonPause.setOnClickListener {
            if (buttonState) {
                buttonPause.text = "Reanudar"
                buttonState = false
            } else {
                buttonPause.text = "Pausar"
                buttonState = true
            }
        }
    }
    private fun buttonGoBack(){
        findViewById<Button>(R.id.buttonGoBackID).setOnClickListener {

        }
    }
}