package com.example.annpurna

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.json.JSONObject
import java.io.File
import java.io.FileWriter

class ProfileUpdateFragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var databaseReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile_update, container, false)

        // Initialize Views
        val usernameEdit = view.findViewById<EditText>(R.id.edit_username)
        val emailTextView = view.findViewById<TextView>(R.id.view_email) // Display only
        val contactTextView = view.findViewById<TextView>(R.id.view_contact) // Display only
        val pincodeEdit = view.findViewById<EditText>(R.id.edit_pincode)
        val stateEdit = view.findViewById<EditText>(R.id.edit_state)
        val cityEdit = view.findViewById<EditText>(R.id.edit_city)
        val addressEdit = view.findViewById<EditText>(R.id.edit_address)
        val updateButton = view.findViewById<Button>(R.id.update_button)

        // Initialize Shared Preferences
        sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        // Initialize Firebase Database Reference
        databaseReference = FirebaseDatabase.getInstance().getReference("user_info")

        // Load existing data into the views
        loadExistingData(
            usernameEdit, emailTextView, contactTextView, pincodeEdit, stateEdit, cityEdit, addressEdit
        )

        // Update Button Click Listener
        updateButton.setOnClickListener {
            val updatedUsername = usernameEdit.text.toString().trim()
            val updatedEmail = emailTextView.text.toString().trim()
            val updatedContact = contactTextView.text.toString().trim()
            val updatedPincode = pincodeEdit.text.toString().trim()
            val updatedState = stateEdit.text.toString().trim()
            val updatedCity = cityEdit.text.toString().trim()
            val updatedAddress = addressEdit.text.toString().trim()

            if (validateFields(updatedPincode, updatedState, updatedCity, updatedAddress)) {
                // Update Firebase and Local Data
                updateFirebase(updatedUsername, updatedEmail, updatedContact, updatedPincode, updatedState, updatedCity, updatedAddress)
                updateLocalFile(updatedUsername, updatedEmail, updatedContact, updatedPincode, updatedState, updatedCity, updatedAddress)
            } else {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    // Function to load existing data into the views
    private fun loadExistingData(
        usernameEdit: EditText,
        emailTextView: TextView,
        contactTextView: TextView,
        pincodeEdit: EditText,
        stateEdit: EditText,
        cityEdit: EditText,
        addressEdit: EditText
    ) {
        // Retrieve saved data from local JSON file
        val jsonFile = File(requireContext().filesDir, "user_data.json")
        if (jsonFile.exists()) {
            val jsonString = jsonFile.readText()
            try {
                val jsonObject = JSONObject(jsonString)
                val username = jsonObject.optString("name", "")
                val email = jsonObject.optString("email", "")
                val contact = jsonObject.optString("contactNumber", "")
                val pincode = jsonObject.optString("pincode", "")
                val state = jsonObject.optString("state", "")
                val city = jsonObject.optString("city", "")
                val address = jsonObject.optString("address", "")

                // Populate the views with existing data
                usernameEdit.setText(username)
                emailTextView.text = email // Display email as TextView
                contactTextView.text = contact // Display contact number as TextView
                pincodeEdit.setText(pincode)
                stateEdit.setText(state)
                cityEdit.setText(city)
                addressEdit.setText(address)

            } catch (e: Exception) {
                Log.e("ProfileUpdateFragment", "Error loading local JSON data", e)
            }
        }
    }

    // Function to validate that all required fields are filled
    private fun validateFields(
        pincode: String, state: String, city: String, address: String
    ): Boolean {
        return pincode.isNotEmpty() && state.isNotEmpty() && city.isNotEmpty() && address.isNotEmpty()
    }

    // Function to update data in Firebase
    private fun updateFirebase(
        username: String, email: String, contact: String,
        pincode: String, state: String, city: String, address: String
    ) {
        // Firebase reference using sanitized email
        val sanitizedEmail = email.replace(".", "_")

        // Create a map for the updated user data
        val userMap = mapOf(
            "name" to username,
            "email" to email,
            "contactNumber" to contact,
            "pincode" to pincode,
            "state" to state,
            "city" to city,
            "address" to address
        )

        // Update Firebase under the sanitized email node
        databaseReference.child(sanitizedEmail).updateChildren(userMap)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Details updated in Firebase", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.e("ProfileUpdateFragment", "Firebase update failed", e)
                Toast.makeText(requireContext(), "Firebase update failed", Toast.LENGTH_SHORT).show()
            }
    }

    // Function to update data in local JSON file
    private fun updateLocalFile(
        username: String, email: String, contact: String,
        pincode: String, state: String, city: String, address: String
    ) {
        try {
            // Get the local JSON file
            val jsonFile = File(requireContext().filesDir, "user_data.json")

            // Create a JSONObject to represent the updated data
            val userObject = JSONObject().apply {
                put("name", username)
                put("email", email)
                put("contactNumber", contact)
                put("pincode", pincode)
                put("state", state)
                put("city", city)
                put("address", address)
            }

            // Write the updated JSON to the local file
            FileWriter(jsonFile).use { writer ->
                writer.write(userObject.toString())
            }

            // Show a toast confirming the local update
            Toast.makeText(requireContext(), "Details updated locally", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.e("ProfileUpdateFragment", "Local file update failed", e)
            Toast.makeText(requireContext(), "Local file update failed", Toast.LENGTH_SHORT).show()
        }
    }
}
