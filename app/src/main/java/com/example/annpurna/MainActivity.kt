package com.example.annpurna

import android.os.Bundle
import android.widget.FrameLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.json.JSONObject
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Initialize Firebase Auth and Database
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val frameLayout = findViewById<FrameLayout>(R.id.frame)

        // Initial fragment load
        if (savedInstanceState == null) {
            checkUserDetailsAndLoadFragment()
        }

        // Bottom navigation item selection listener
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
        loadUserNameFromLocalJson(name)
    }

    private fun loadUserNameFromLocalJson(nameTextView: TextView) {
        val userFile = File(filesDir, "user_data.json")

        if (userFile.exists()) {
            val userJson = JSONObject(userFile.readText())
            val username = userJson.optString("name", "User")
            nameTextView.text = "Hello!! $username"
        } else {
            nameTextView.text = "Hello!! User"
        }
    }

    private fun checkUserDetailsAndLoadFragment() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            // Check the user details in Firebase
            database.child("user_info").child(userId).get().addOnSuccessListener { snapshot ->
                val user = snapshot.getValue(User::class.java)
                if (user == null || user.address == null || user.pincode == null || user.state == null || user.city == null) {
                    // Redirect to ProfileFragment if address details are missing
                    supportFragmentManager.beginTransaction().replace(R.id.frame, ProfileFragment()).commit()
                } else {
                    // Otherwise, proceed with the DonorFragment
                    supportFragmentManager.beginTransaction().replace(R.id.frame, DonorFragment()).commit()
                }
            }.addOnFailureListener {
                // Handle failure (e.g., network error)
                supportFragmentManager.beginTransaction().replace(R.id.frame, ProfileFragment()).commit()
            }
        }
    }
}
