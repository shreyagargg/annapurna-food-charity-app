package com.example.annpurna

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val min = 4000L // 4 seconds

        Handler(Looper.getMainLooper()).postDelayed({
            navigateToNextActivity()
        }, min)
    }
//            if(isUserAuthenticated()){
//                val mainIntent = Intent(this@SplashActivity, MainActivity::class.java)
//                startActivity(mainIntent)
//                finish()
//            }
//            else {
//
//                val mainIntent = Intent(this@SplashActivity, LoginActivity::class.java)
//                startActivity(mainIntent)
//                finish()
//            }


    fun isUserAuthenticated(): Boolean {
        return FirebaseAuth.getInstance().currentUser != null
    }

    private fun navigateToNextActivity() {
        val nextActivity = if (isUserAuthenticated()) MainActivity::class.java else LoginActivity::class.java
        startActivity(Intent(this, nextActivity))
        finish()
    }



}