package com.example.annpurna

import android.os.Bundle
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
        bottomNavigation.setSelectedItemId(R.id.donate)

        bottomNavigation.setOnItemSelectedListener { item->
            when(item.itemId){
                R.id.donate ->{
                    supportFragmentManager.beginTransaction().replace(R.id.frame, DonorFragment()).commit()
                    true
                    }
                R.id.accept ->{
                    supportFragmentManager.beginTransaction().replace(R.id.frame, ReceiverFragment()).commit()
                    true
                }
                R.id.volunteer ->{
                    supportFragmentManager.beginTransaction().replace(R.id.frame, ReceiverFragment()).commit()
                    true
                }
                R.id.profile ->{
                    supportFragmentManager.beginTransaction().replace(R.id.frame, ReceiverFragment()).commit()
                    true
                }

                else -> { false }
            }

        }

    }


}