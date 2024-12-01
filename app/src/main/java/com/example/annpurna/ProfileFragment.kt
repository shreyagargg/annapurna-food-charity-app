package com.example.annpurna

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.tabs.TabLayout
import org.json.JSONObject
import java.io.File

class ProfileFragment : Fragment() {

    private lateinit var tabLayout: TabLayout
    private lateinit var fragmentContainer: View
    private lateinit var profileImage: ImageView
    private lateinit var profileName: TextView
    private lateinit var profilePhone: TextView
    private lateinit var profileEmail: TextView
    private lateinit var logoutButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Initialize UI components
        tabLayout = view.findViewById(R.id.tabLayout)
        fragmentContainer = view.findViewById(R.id.fragment_container)
        profileImage = view.findViewById(R.id.profile_image)
        profileName = view.findViewById(R.id.profile_name)
        profilePhone = view.findViewById(R.id.profile_number)
        profileEmail = view.findViewById(R.id.profile_email)
        logoutButton = view.findViewById(R.id.logout_button)

        // Load user data from local storage
        loadUserData()

        // Set up TabLayout functionality
        setupTabLayout()

        // Set Logout button functionality
        logoutButton.setOnClickListener {
            clearLocalData()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

        return view
    }

    private fun loadUserData() {
        val file = File(requireContext().filesDir, "user_data.json")
        if (file.exists()) {
            val jsonString = file.readText()
            // Log the raw JSON content
            Log.d("ProfileFragment", "JSON String: $jsonString")

            try {
                val jsonObject = JSONObject(jsonString)

                // Update the key names to match the JSON structure
                val name = jsonObject.optString("name", "N/A")
                val contactNumber = jsonObject.optString("contactNumber", "N/A")
                val email = jsonObject.optString("email", "N/A")

                // Log the extracted values
                Log.d("ProfileFragment", "Name: $name")
                Log.d("ProfileFragment", "Contact Number: $contactNumber")
                Log.d("ProfileFragment", "Email: $email")

                // Update UI components with the loaded data
                profileName.text = name
                profilePhone.text = contactNumber
                profileEmail.text = email
            } catch (e: Exception) {
                Log.e("ProfileFragment", "Error parsing JSON", e)
            }
        } else {
            Log.e("ProfileFragment", "User data file does not exist.")
        }
    }

    private fun setupTabLayout() {
        // Load the default tab (History)
        loadFragment(HistoryFragment())

        // Set TabLayout listeners
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> loadFragment(HistoryFragment()) // History tab
                    1 -> loadFragment(ProfileUpdateFragment()) // Edit Profile tab
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun loadFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = parentFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }

    private fun clearLocalData() {
        // Clear shared preferences
        val sharedPreferences = requireContext().getSharedPreferences("UserDetails", Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()

        // Clear local JSON file
        val file = File(requireContext().filesDir, "user_data.json")
        if (file.exists()) {
            val isDeleted = file.delete()
            if (isDeleted) {
                Log.d("ProfileFragment", "Local JSON file deleted successfully.")
            } else {
                Log.e("ProfileFragment", "Failed to delete local JSON file.")
            }
        } else {
            Log.d("ProfileFragment", "No local JSON file to delete.")
        }

        Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()
    }
}
