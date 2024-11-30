package com.example.annpurna

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class DonorFragment : Fragment() {

    private lateinit var database: DatabaseReference
    private lateinit var storage: StorageReference
    private var imageUri: Uri? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var donorAdapter: DonorAdapter
    private var donorList: ArrayList<DonorModel> = ArrayList()

    private lateinit var addMoreButton: Button
    private lateinit var donateButton: Button
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Firebase instance initialization
        database = FirebaseDatabase.getInstance().reference
        storage = FirebaseStorage.getInstance().reference
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_donor, container, false)

        // Initialize views
        recyclerView = view.findViewById(R.id.recycler_view)
        addMoreButton = view.findViewById(R.id.addMore)
        donateButton = view.findViewById(R.id.donate)

        // Initialize SharedPreferences for local storage
        sharedPreferences = requireContext().getSharedPreferences("UserDetails", Context.MODE_PRIVATE)

        // Set up RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)
        donorAdapter = DonorAdapter(donorList)
        recyclerView.adapter = donorAdapter

        // Add the initial donation form
        addNewForm()

        // Add More button click listener
        addMoreButton.setOnClickListener {
            addNewForm()
        }

        // Donate button click listener (to save data in Firebase)
        donateButton.setOnClickListener {
            checkUserAddressAndSaveData()
        }

        return view
    }

    private fun addNewForm() {
        // Add a new empty form to the donor list
        donorList.add(DonorModel("", "", "", "kg", "")) // Empty fields for the new form
        donorAdapter.notifyItemInserted(donorList.size - 1)
    }

    private fun checkUserAddressAndSaveData() {
        // Retrieve user address info from local storage
        val userAddress = sharedPreferences.getString("address", null)
        val userCity = sharedPreferences.getString("city", null)

        // If address info is not available in local storage, check Firebase
        if (userAddress == null || userCity == null) {
            // Check if user address info exists in Firebase
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            if (userId != null) {
                database.child("user_info").child(userId).get().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userInfo = task.result
                        val firebaseAddress = userInfo?.child("address")?.value as? String
                        val firebaseCity = userInfo?.child("city")?.value as? String

                        if (firebaseAddress.isNullOrEmpty() || firebaseCity.isNullOrEmpty()) {
                            // If the address or city is missing, send to ProfileFragment
                            Toast.makeText(requireContext(), "Please update your profile", Toast.LENGTH_SHORT).show()
                            // Redirect to profile fragment
                            activity?.supportFragmentManager?.beginTransaction()
                                ?.replace(R.id.frame, ProfileFragment())
                                ?.addToBackStack(null)
                                ?.commit()
                        } else {
                            // Address and city are available, save donation data
                            saveAllData()
                        }
                    } else {
                        Toast.makeText(requireContext(), "Failed to fetch user info", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(requireContext(), "User not authenticated", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Address info available locally, save donation data
            saveAllData()
        }
    }

    private fun saveAllData() {
        for (donor in donorList) {
            if (donor.name.isNotBlank() && donor.description.isNotBlank() &&
                donor.date.isNotBlank() && donor.quantity.isNotBlank()) {
                saveData(donor)
            } else {
                Toast.makeText(requireContext(), "Mark entries first", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveData(donor: DonorModel) {
        val donationId = database.child("Donations").push().key ?: return

        // Saving data to Firebase
        val donationData = mapOf(
            "foodItem" to donor.name,
            "description" to donor.description,
            "date" to donor.date,
            "quantity" to donor.quantity
        )

        // Save to Firebase database
        database.child("Donations").child(donationId).setValue(donationData)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "Donation data saved successfully", Toast.LENGTH_SHORT).show()
                    // Optionally upload an image with the donation
                    uploadImage(donationId)
                } else {
                    Toast.makeText(requireContext(), "Failed to save donation data", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun uploadImage(donationId: String) {
        imageUri?.let { uri ->
            val imageRef = storage.child("donations/$donationId/image.jpg")
            imageRef.putFile(uri).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "Image uploaded successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Image upload failed", Toast.LENGTH_SHORT).show()
                }
            }
        } ?: Toast.makeText(requireContext(), "No image selected", Toast.LENGTH_SHORT).show()
    }
}
