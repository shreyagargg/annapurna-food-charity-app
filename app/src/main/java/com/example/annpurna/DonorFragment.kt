package com.example.annpurna

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import org.json.JSONObject
import java.io.File

class DonorFragment : Fragment() {

    private lateinit var database: DatabaseReference
    private lateinit var storage: StorageReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var donorAdapter: DonorAdapter
    private lateinit var addMoreButton: Button
    private lateinit var donateButton: Button
    private var donorList: ArrayList<DonorModel> = ArrayList()
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize Firebase instances
        database = FirebaseDatabase.getInstance().reference
        storage = FirebaseStorage.getInstance().reference
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_donor, container, false)

        recyclerView = view.findViewById(R.id.recycler_view)
        addMoreButton = view.findViewById(R.id.addMore)
        donateButton = view.findViewById(R.id.donate)

        // Set up RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)
        donorAdapter = DonorAdapter(donorList)
        recyclerView.adapter = donorAdapter

        // Add an initial donation form
        addNewForm()

        // Button click listeners
        addMoreButton.setOnClickListener { addNewForm() }
        donateButton.setOnClickListener { fetchUserDetailsAndSaveDonations() }

        return view
    }

    private fun addNewForm() {
        donorList.add(DonorModel("", "", "", "kg", ""))
        donorAdapter.notifyItemInserted(donorList.size - 1)
    }

    private fun fetchUserDetailsAndSaveDonations() {
        val userFile = File(requireContext().filesDir, "user_data.json")
        if (userFile.exists()) {
            // Read user data from local storage
            val userData = JSONObject(userFile.readText())
            val name = userData.optString("name", null)
            val contactNumber = userData.optString("contactNumber", null)
            val city = userData.optString("city", null)
            val address = userData.optString("address", null)

            if (name != null && contactNumber != null && city != null && address != null) {
                // Save donations using local data
                saveDonations(name, contactNumber, city, address)
            } else {
                // Fetch data from Firebase if local storage is incomplete
                fetchDetailsFromFirebaseAndSave()
            }
        } else {
            // Fetch data from Firebase if local file is missing
            fetchDetailsFromFirebaseAndSave()
        }
    }

    private fun fetchDetailsFromFirebaseAndSave() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            database.child("user_info").child(userId).get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userInfo = task.result
                    val name = userInfo?.child("name")?.value as? String
                    val contactNumber = userInfo?.child("contactNumber")?.value as? String
                    val city = userInfo?.child("city")?.value as? String
                    val address = userInfo?.child("address")?.value as? String

                    if (!name.isNullOrEmpty() && !contactNumber.isNullOrEmpty() && !city.isNullOrEmpty() && !address.isNullOrEmpty()) {
                        // Save donations using Firebase data
                        saveDonations(name, contactNumber, city, address)
                    } else {
                        // Redirect to ProfileFragment if data is incomplete
                        Toast.makeText(requireContext(), "Please update your profile", Toast.LENGTH_SHORT).show()
                        activity?.supportFragmentManager?.beginTransaction()
                            ?.replace(R.id.frame, ProfileFragment())
                            ?.addToBackStack(null)
                            ?.commit()
                    }
                } else {
                    Toast.makeText(requireContext(), "Failed to fetch user data", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(requireContext(), "User not authenticated", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveDonations(donorName: String, donorContact: String, donorCity: String, donorAddress: String) {
        var allValid = true
        var uploadedCount = 0

        for (donor in donorList) {
            if (donor.name.isNotBlank() && donor.description.isNotBlank() &&
                donor.date.isNotBlank() && donor.quantity.isNotBlank()
            ) {
                uploadedCount++
                saveDonationToFirebase(donor, donorName, donorContact, donorCity, donorAddress) {
                    uploadedCount--
                    if (uploadedCount == 0) resetDonorFragment()
                }
            } else {
                allValid = false
            }
        }

        if (!allValid) {
            Toast.makeText(requireContext(), "Please complete all fields before donating.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveDonationToFirebase(
        donor: DonorModel,
        donorName: String,
        donorContact: String,
        donorCity: String,
        donorAddress: String,
        onComplete: () -> Unit
    ) {
        val donationId = database.child("Donations").push().key ?: return
        val donationData = Donations(
            Dname = donorName,
            Dcontact = donorContact,
            Dcity = donorCity,
            Daddress = donorAddress,
            foodItem = donor.name,
            description = donor.description,
            expiryDate = donor.date,
            quantityType = donor.quantityType,
            quantity = donor.quantity,
            perishable = "Yes" // Default value; change if needed
        )

        database.child("Donations").child(donationId).setValue(donationData)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    uploadDonationImage(donationId, onComplete)
                } else {
                    Toast.makeText(requireContext(), "Failed to save donation.", Toast.LENGTH_SHORT).show()
                    onComplete()
                }
            }
    }

    private fun uploadDonationImage(donationId: String, onComplete: () -> Unit) {
        imageUri?.let { uri ->
            val imageRef = storage.child("donations/$donationId/image.jpg")
            imageRef.putFile(uri).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "Image uploaded successfully!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Failed to upload image.", Toast.LENGTH_SHORT).show()
                }
                onComplete()
            }
        } ?: onComplete()
    }

    private fun resetDonorFragment() {
        donorList.clear()
        addNewForm()
        donorAdapter.notifyDataSetChanged()

        Toast.makeText(requireContext(), "Donations uploaded and form cleared!", Toast.LENGTH_SHORT).show()
    }
}
