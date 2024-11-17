package com.example.annpurna

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class TermsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_terms)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val check = findViewById<CheckBox>(R.id.check)
        val accept = findViewById<Button>(R.id.accept)

        accept.visibility = View.GONE

        check.setOnCheckedChangeListener { _, isChecked ->
            // If the CheckBox is checked, show and enable the Accept button
            if (isChecked) {
                accept.isEnabled = true  // Enable the button
                accept.visibility = View.VISIBLE  // Show the button
            } else {
                accept.isEnabled = false  // Disable the button
                accept.visibility = View.GONE  // Hide the button
            }
        }

        accept.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
            finish()
        }
    }
}