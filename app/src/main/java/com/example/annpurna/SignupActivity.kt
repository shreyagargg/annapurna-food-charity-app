package com.example.annpurna

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val email = findViewById<EditText>(R.id.mail)
        val password = findViewById<EditText>(R.id.pass)
        val name = findViewById<EditText>(R.id.name)
        val phn = findViewById<EditText>(R.id.phn)

        val signup = findViewById<Button>(R.id.sign)
        val bar = findViewById<ProgressBar>(R.id.bar)
        val fauth : FirebaseAuth = FirebaseAuth.getInstance()
        val login = findViewById<TextView>(R.id.login)
        val hide = findViewById<Switch>(R.id.hide)

        signup.setOnClickListener {
            val mail: String = email.text.toString().trim()
            val pass: String = password.text.toString().trim()
            val Name: String = name.text.toString().trim()
            val phone: String = phn.text.toString().trim()

            if (TextUtils.isEmpty(mail)) {
                email.error = "Required section"
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(pass)) {
                password.error = "Required section"
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(Name)) {
                name.error = "Required section"
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(phone)) {
                phn.error = "Required section"
                return@setOnClickListener
            }
            saveData(Name,mail,pass)

            bar.setVisibility(View.VISIBLE)

            fauth.createUserWithEmailAndPassword(mail, pass)
                .addOnCompleteListener(this) { task ->
                    bar.visibility = ProgressBar.GONE

                    if (task.isSuccessful) {
                        Toast.makeText(this, "Sign up successful", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    } else {
                        val errorMessage = task.exception?.message ?: "Sign up failed"
                        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()

                    }
                }
        }

        login.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        fun togglePasswordVisibility(isVisible: Boolean) {
            if (isVisible) {
                password.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                hide.text = "Hide Password"
            } else {
                password.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                hide.text = "Show Password"
            }
            password.setSelection(password.text.length)
        }

        hide.setOnCheckedChangeListener { _, isChecked ->
            togglePasswordVisibility(isChecked)
        }
    }

    private fun saveData(mail: String, text2: String, text3: String) {
        val sharedPreferences: SharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString("savedText1", mail)
        editor.putString("savedText2", text2)
        editor.putString("savedText3", text3)

        editor.apply()
    }
}