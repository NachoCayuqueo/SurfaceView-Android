package com.example.surfaceview

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userInterface()
    }

    private fun userInterface(){
        val editText = findViewById<EditText>(R.id.editTextID)

        findViewById<Button>(R.id.buttonID).setOnClickListener {
            val username = editText.text.toString()

            if(username != ""){
                val intent = Intent(this,AnimationActivity::class.java)
                intent.putExtra("username",username)
                startActivity(intent)
            }else{
                Toast.makeText(this,"Debe ingresar un nombre",
                    Toast.LENGTH_SHORT).show()
                editText.requestFocus()

                val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(editText,InputMethodManager.SHOW_IMPLICIT)
            }
        }
    }
}