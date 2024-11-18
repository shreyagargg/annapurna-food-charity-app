package com.example.annpurna

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class ProfileFragment : Fragment() {

    private lateinit var database: DatabaseReference
    private lateinit var storage: StorageReference
    private lateinit var pickImage: ActivityResultLauncher<Intent>
    private var imageUri: Uri? = null
    private lateinit var csvReader: CsvReader
    private lateinit var locationList: List<Person>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database = FirebaseDatabase.getInstance().reference
        storage = FirebaseStorage.getInstance().reference
        csvReader = CsvReader()

        locationList = csvReader.readCSVFromRaw(requireContext(), R.raw.city)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val name = view.findViewById<TextView>(R.id.name)
        val contact = view.findViewById<TextView>(R.id.phn)
        val mail = view.findViewById<TextView>(R.id.mail)
        val code = view.findViewById<EditText>(R.id.pc)
        val city = view.findViewById<TextView>(R.id.city)
        val address = view.findViewById<EditText>(R.id.address)
        val update = view.findViewById<Button>(R.id.update)
        val image = view.findViewById<ImageView>(R.id.profile)

        image.setOnClickListener {
            // Create a bundle for the fragment transition
//            val bundle = Bundle()

            // Create a FragmentTransaction with shared element
            val transaction = requireActivity().supportFragmentManager.beginTransaction()

            // Specify the shared element transition
            transaction.setReorderingAllowed(true)
            transaction.addSharedElement(image, "shared_image")
            transaction.addSharedElement(name, "name")

            val destinationFragment = ProfileView()

            // Optional: Add data to the destination fragment (if needed)
            val bundle = Bundle()
            destinationFragment.arguments = bundle

            transaction.replace(R.id.frame, destinationFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }


        update.setOnClickListener {
            val pincode = code.text.toString().trim()

            if (pincode.isNotEmpty()) {
                val location = locationList.find { it.pincode == pincode }
                Toast.makeText(context,"$location", Toast.LENGTH_SHORT).show()

                if (location != null) {
                    city.text = "${location.state} / ${location.location}"
                } else {
                    city.text = "Not Found"
                }
            } else {
                city.text = "Please enter a valid pincode."
            }
        }


        return view
    }


        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
