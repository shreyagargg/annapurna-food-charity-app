package com.example.annpurna

import android.content.Intent
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

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val email = findViewById<EditText>(R.id.mail)
        val password = findViewById<EditText>(R.id.pass)
        val login = findViewById<Button>(R.id.signIn)
        val bar = findViewById<ProgressBar>(R.id.bar)
        val fauth : FirebaseAuth = FirebaseAuth.getInstance()
        val register = findViewById<TextView>(R.id.register)
        val forgetPass = findViewById<TextView>(R.id.forgetPassword)
        val hide = findViewById<Switch>(R.id.hide)


        login.setOnClickListener{

            val mail: String = email.text.toString().trim()
            val pass: String = password.text.toString().trim()

            if (TextUtils.isEmpty(mail)) {
                email.error = "Required section"
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(pass)) {
                password.error = "Required section"
                return@setOnClickListener
            }

            bar.setVisibility(View.VISIBLE)

            fauth.signInWithEmailAndPassword(mail,pass).addOnCompleteListener(this) {
                    task -> bar.visibility = ProgressBar.GONE
                if (task.isSuccessful) {
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    val errorMessage = task.exception?.message ?: "Login failed"
                    Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()

                }
            }
        }

        register.setOnClickListener{
            startActivity(Intent(this, SignupActivity::class.java))
            overridePendingTransition(android.R.anim.cycle_interpolator,android.R.anim.fade_out)
            finish()
        }

        fun resetPassword(email: String) {
            fauth.sendPasswordResetEmail(email)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Password reset email sent.", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this,"Failed to send password reset email.", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        forgetPass.setOnClickListener{
            val mail: String = email.text.toString().trim()
                if (mail.isNotEmpty()) {
                    resetPassword(mail)
                } else {
                    Toast.makeText(this,"Please enter your email.", Toast.LENGTH_SHORT).show()
                }
        }

        fun togglePasswordVisibility(isVisible: Boolean) {
            if (isVisible) {
                // Show password
                password.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                hide.text = "Hide Password"
            } else {
                // Hide password
                password.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                hide.text = "Show Password"
            }
            // Move the cursor to the end of the text
            password.setSelection(password.text.length)
        }

        hide.setOnCheckedChangeListener { _, isChecked ->
            togglePasswordVisibility(isChecked)
        }


    }

}