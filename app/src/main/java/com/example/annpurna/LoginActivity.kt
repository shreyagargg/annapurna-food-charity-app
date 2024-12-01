package com.example.annpurna

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.json.JSONObject
import java.io.File

class LoginActivity : AppCompatActivity() {

    private lateinit var fauth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val emailField = findViewById<EditText>(R.id.mail)
        val passwordField = findViewById<EditText>(R.id.pass)
        val loginButton = findViewById<Button>(R.id.signIn)
        val progressBar = findViewById<ProgressBar>(R.id.bar)
        val registerLink = findViewById<TextView>(R.id.register)
        val forgetPasswordLink = findViewById<TextView>(R.id.forgetPassword)
        val hideSwitch = findViewById<Switch>(R.id.hide)

        fauth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        loginButton.setOnClickListener {
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()

            if (TextUtils.isEmpty(email)) {
                emailField.error = "Required section"
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(password)) {
                passwordField.error = "Required section"
                return@setOnClickListener
            }

            progressBar.visibility = View.VISIBLE

            fauth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                progressBar.visibility = ProgressBar.GONE
                if (task.isSuccessful) {
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                    fetchAndUpdateLocalStorage(email)
                } else {
                    val errorMessage = task.exception?.message ?: "Login failed"
                    Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
                }
            }
        }

        registerLink.setOnClickListener {
            startActivity(Intent(this, TermsActivity::class.java))
            finish()
        }

        forgetPasswordLink.setOnClickListener {
            val email = emailField.text.toString().trim()
            if (email.isNotEmpty()) {
                resetPassword(email)
            } else {
                Toast.makeText(this, "Please enter your email.", Toast.LENGTH_SHORT).show()
            }
        }

        hideSwitch.setOnCheckedChangeListener { _, isChecked ->
            togglePasswordVisibility(isChecked)
        }
    }

    private fun resetPassword(email: String) {
        fauth.sendPasswordResetEmail(email)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Password reset email sent.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Failed to send password reset email.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun togglePasswordVisibility(isVisible: Boolean) {
        val passwordField = findViewById<EditText>(R.id.pass)
        passwordField.inputType = if (isVisible) {
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        } else {
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
        passwordField.setSelection(passwordField.text.length)
    }

    private fun fetchAndUpdateLocalStorage(email: String) {
        val sanitizedEmail = email.replace(".", "_")
        database.child("user_info").child(sanitizedEmail).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val userDetails = task.result

                val username = userDetails?.child("name")?.value as? String ?: "null"
                val email = userDetails?.child("email")?.value as? String ?: "null"
                val contactNumber = userDetails?.child("contactNumber")?.value as? String ?: "null"
                val password = userDetails?.child("password")?.value as? String ?: "null"
                val address = userDetails?.child("address")?.value as? String ?: "null"
                val pincode = userDetails?.child("pincode")?.value as? String ?: "null"
                val state = userDetails?.child("state")?.value as? String ?: "null"
                val city = userDetails?.child("city")?.value as? String ?: "null"

                saveUserDetailsToLocalStorage(username, email, contactNumber, password, address, pincode, state, city)

                // Navigate to ProfileFragment (via MainActivity)
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("redirectTo", "profileFragment")
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Failed to fetch user details", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveUserDetailsToLocalStorage(
        username: String,
        email: String,
        contactNumber: String,
        password: String,
        address: String,
        pincode: String,
        state: String,
        city: String
    ) {
        val jsonObject = JSONObject()
        jsonObject.put("name", username)
        jsonObject.put("email", email)
        jsonObject.put("contactNumber", contactNumber)
        jsonObject.put("password", password)
        jsonObject.put("address", address)
        jsonObject.put("pincode", pincode)
        jsonObject.put("state", state)
        jsonObject.put("city", city)

        val file = File(filesDir, "user_data.json")
        file.writeText(jsonObject.toString())
    }
}
