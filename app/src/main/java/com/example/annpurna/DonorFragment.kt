package com.example.annpurna

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
            saveAllData()
        }

        return view
    }

    private fun addNewForm() {
        // Add a new empty form to the donor list
        donorList.add(DonorModel("", "", "", "kg", "")) // Empty fields for the new form
        donorAdapter.notifyItemInserted(donorList.size - 1)
        Toast.makeText(requireContext(), "new view", Toast.LENGTH_SHORT).show()

    }

    private fun saveAllData() {
        for (donor in donorList) {
            if (donor.name.isNotBlank() && donor.description.isNotBlank() &&
                donor.date.isNotBlank() && donor.quantity.isNotBlank()) {
                saveData(donor)
            }
            else
                Toast.makeText(requireContext(), "Mark entries first", Toast.LENGTH_SHORT).show()

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
                    Toast.makeText(requireContext(), "Successfull to save data", Toast.LENGTH_SHORT).show()

                    // Uncomment if you want to upload an image with the donation
//                     uploadImage(donationId)
                } else {
                    Toast.makeText(requireContext(), "Failed to save data", Toast.LENGTH_SHORT).show()
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
