package com.example.kotlinapps

import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivityluckynumber : AppCompatActivity() {

    private lateinit var editTXT: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_activityluckynumber)

        val bl: Button = findViewById(R.id.btn)
        editTXT = findViewById(R.id.edtTxt)

        // Filter to allow ONLY letters and spaces
        val filter = InputFilter { source, start, end, _, _, _ ->
            for (i in start until end) {
                if (!Character.isLetter(source[i]) && source[i] != ' ') {
                    return@InputFilter ""
                }
            }
            null
        }
        editTXT.filters = arrayOf(filter)

        bl.setOnClickListener {
            val username = editTXT.text.toString().trim()

            if (username.isEmpty()) {
                editTXT.error = "Name is required!"
            } else if (username.length < 3) {
                editTXT.error = "Name must be at least 3 characters"
            } else {
                val i = Intent(this, MainActivityrandomluckynumber::class.java)
                i.putExtra("name", username)
                startActivity(i)
            }
        }
    }

    // Automatically clears the name field when you come back to this page
    override fun onResume() {
        super.onResume()
        if (::editTXT.isInitialized) {
            editTXT.text.clear()
        }
    }
}