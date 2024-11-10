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
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val name = view.findViewById<TextView>(R.id.name)
        val contact = view.findViewById<TextView>(R.id.phn)
        val mail = view.findViewById<TextView>(R.id.mail)
        val code = view.findViewById<EditText>(R.id.pc)
        val city = view.findViewById<TextView>(R.id.city)
        val address = view.findViewById<EditText>(R.id.address)
        val update = view.findViewById<Button>(R.id.update)


//        val data = csvReader.readCSVFromRaw(this, R.raw.city)

//        if(code.isNotEmpty()){
//            city.text = data[1].state
//        }
//        else
//            city.text = "State and City"


//        val locationList = csvReader.readCSVFromRaw(requireContext(), R.raw.city)

        update.setOnClickListener {
            val pincode = code.text.toString().trim()

            if (pincode.isNotEmpty()) {
                // Find the location by pincode
                val location = locationList.find { it.pincode == pincode }
                Toast.makeText(context,"$location", Toast.LENGTH_SHORT).show()

                if (location != null) {
                    // Display state and city
                    city.text = "${location.state} / ${location.location}"
//                    cityTextView.text = "City: ${location.city}"
                } else {
                    // If no matching location is found
                    city.text = "Not Found"
//                    cityTextView.text = "City: Not Found"
                }
            } else {
                // Handle empty input
                city.text = "Please enter a valid pincode."
//                cityTextView.text = ""
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
