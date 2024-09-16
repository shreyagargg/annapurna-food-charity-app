package com.example.annpurna

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class DonorFragment : Fragment() {

    private lateinit var database: DatabaseReference
    private lateinit var storage: StorageReference
    private lateinit var pickImage: ActivityResultLauncher<Intent>
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database = FirebaseDatabase.getInstance().reference
        storage = FirebaseStorage.getInstance().reference

//        pickImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            if (result.resultCode == Activity.RESULT_OK) {
//                imageUri = result.data?.data
//                view?.findViewById<ImageView>(R.id.image)?.setImageURI(imageUri)
//            }
//        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_donor, container, false)

        val foodItemEditText: EditText = view.findViewById(R.id.foodItem)
        val descriptionEditText: EditText = view.findViewById(R.id.foodDescrip)
        val dateEditText: EditText = view.findViewById(R.id.date)
        val stateEditText: EditText = view.findViewById(R.id.state)
        val cityEditText: EditText = view.findViewById(R.id.city)
        val codeEditText: EditText = view.findViewById(R.id.code)
        val donateButton: Button = view.findViewById(R.id.donate)
//        val imageButton: ImageButton = view.findViewById(R.id.image)

        donateButton.setOnClickListener {
            val foodItem = foodItemEditText.text.toString()
            val description = descriptionEditText.text.toString()
            val dateExp = dateEditText.text.toString()
            val stateName = stateEditText.text.toString()
            val cityName = cityEditText.text.toString()
            val codeN = codeEditText.text.toString()

            if (foodItem.isNotBlank() && description.isNotBlank() && dateExp.isNotBlank()
                && stateName.isNotBlank() && cityName.isNotBlank() && codeN.isNotBlank()
            ) {
                saveData(foodItem, description, dateExp, stateName, cityName, codeN)
                Toast.makeText(
                    requireContext(),
                    "Donation of $foodItem Successful",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(requireContext(), "Please fill all columns", Toast.LENGTH_SHORT)
                    .show()
            }
        }

//        imageButton.setOnClickListener {
//            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//            pickImage.launch(intent)
//        }

        return view
    }

    private fun saveData(
        foodItem: String,
        description: String,
        dateExp: String,
        stateName: String,
        cityName: String,
        codeN: String
    ) {
        val donationId = database.child("Donations").push().key ?: return

        val donationData = mapOf(
            "foodItem" to foodItem,
            "description" to description,
            "date" to dateExp,
            "state" to stateName,
            "city" to cityName,
            "pinCode" to codeN
        )

        database.child("Donations").child(donationId).setValue(donationData)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    uploadImage(donationId)
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
