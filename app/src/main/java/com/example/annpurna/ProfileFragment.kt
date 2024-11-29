package com.example.annpurna

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
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
        val state = view.findViewById<TextView>(R.id.state)
        val address = view.findViewById<EditText>(R.id.address)
        val update = view.findViewById<Button>(R.id.update)
        val image = view.findViewById<ImageView>(R.id.profile)
        val hist = view.findViewById<Button>(R.id.hist)
        val edit = view.findViewById<Button>(R.id.edit)
        val logoutButton = view.findViewById<Button>(R.id.logout)

        loadData(name, contact, mail)

        image.setOnClickListener {
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


        state.setOnClickListener {
            val pincode = code.text.toString().trim()

            if (pincode.isNotEmpty()) {
                val location = locationList.find { it.pincode == pincode }
                Toast.makeText(context,"$location", Toast.LENGTH_SHORT).show()

                if (location != null) {
                    state.text = "${location.state}"
                } else {
                    state.text = "Not Found"
                }
            } else {
                state.text = "Please enter a valid pincode."
            }
        }

        logoutButton.setOnClickListener {
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }


        return view
    }

    private fun loadData(name: TextView, contact: TextView, mail: TextView) {
        val sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE)

        val savedName = sharedPreferences.getString("savedText1", "Default Name")
        val savedContact = sharedPreferences.getString("savedText2", "Default Contact")
        val savedMail = sharedPreferences.getString("savedText3", "Default Email")

        name.text = savedName
        contact.text = savedContact
        mail.text = savedMail
    }


}
