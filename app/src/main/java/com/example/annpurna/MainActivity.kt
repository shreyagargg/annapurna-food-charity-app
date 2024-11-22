package com.example.annpurna

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val frameLayout = findViewById<FrameLayout>(R.id.frame)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.frame, DonorFragment())
                .commit()
        }

        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.donate -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frame, DonorFragment()).commit()
                    true
                }
                R.id.accept -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frame, ReceiverFragment()).commit()
                    true
                }
                R.id.volunteer -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frame, VolunteerFragment()).commit()
                    true
                }
                R.id.profile -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frame, ProfileFragment()).commit()
                    true
                }
                else -> false
            }
        }

        val name = findViewById<TextView>(R.id.name)
        loadSavedData(name)
    }

    private fun loadSavedData(data: TextView) {
        val sharedPreferences: SharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val savedText = sharedPreferences.getString("savedText", "data")
        data.text = "Hello $savedText"
    }
}

