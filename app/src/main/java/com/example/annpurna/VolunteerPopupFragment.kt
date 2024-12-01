package com.example.annpurna

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.json.JSONObject
import java.io.File

class VolunteerPopupFragment : DialogFragment() {

    private lateinit var sourceAddressTextView: TextView
    private lateinit var destinationAddressTextView: TextView
    private lateinit var deliverButton: Button

    private lateinit var database: DatabaseReference
    private var volunteerModel: VolunteerModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FirebaseDatabase.getInstance().reference

        // Retrieve passed VolunteerModel
        arguments?.let {
            volunteerModel = it.getParcelable(ARG_VOLUNTEER_MODEL)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_volunteer_popup, container, false)

        sourceAddressTextView = view.findViewById(R.id.popup_source_address)
        destinationAddressTextView = view.findViewById(R.id.popup_destination_address)
        deliverButton = view.findViewById(R.id.deliver_button)

        // Populate Source and Destination Addresses
        volunteerModel?.let {
            sourceAddressTextView.text = "Source Address: ${it.Saddress}"
            destinationAddressTextView.text = "Destination Address: ${it.Daddress}"
        }

        // Handle Deliver Button Click
        deliverButton.setOnClickListener {
            volunteerModel?.let { model ->
                assignDelivery(model)
            }
        }

        return view
    }

    private fun assignDelivery(model: VolunteerModel) {
        // Read user data from local JSON
        val userData = getUserDataFromLocal(requireContext())

        if (userData != null) {
            val volunteerName = userData["name"] ?: ""
            val volunteerContact = userData["contactNumber"] ?: ""

            if (volunteerName.isNotEmpty() && volunteerContact.isNotEmpty()) {
                val donationRef = database.child("Donations").child(model.Dname + "_" + model.Dcontact)

                // Update Vname, Vcontact, and Delivered fields
                val updates = mapOf(
                    "Vname" to volunteerName,
                    "Vcontact" to volunteerContact,
                    "Delivered" to 1
                )

                donationRef.updateChildren(updates).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(requireContext(), "Delivery assigned successfully!", Toast.LENGTH_SHORT).show()
                        dismiss()
                    } else {
                        Toast.makeText(requireContext(), "Failed to assign delivery.", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Failed to retrieve volunteer details.", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "User data file not found.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getUserDataFromLocal(context: Context): Map<String, String>? {
        val fileName = "user_data.json"
        val file = File(context.filesDir, fileName)

        return if (file.exists()) {
            val jsonString = file.readText()
            val jsonObject = JSONObject(jsonString)

            mapOf(
                "name" to jsonObject.optString("name", ""),
                "contactNumber" to jsonObject.optString("contactNumber", "")
            )
        } else {
            null
        }
    }

    companion object {
        private const val ARG_VOLUNTEER_MODEL = "volunteer_model"

        fun newInstance(model: VolunteerModel): VolunteerPopupFragment {
            val fragment = VolunteerPopupFragment()
            val args = Bundle().apply {
                putParcelable(ARG_VOLUNTEER_MODEL, model)
            }
            fragment.arguments = args
            return fragment
        }
    }
}
//package com.example.annpurna
//
//import android.content.Context
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Button
//import android.widget.TextView
//import android.widget.Toast
//import androidx.fragment.app.DialogFragment
//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.FirebaseDatabase
//import org.json.JSONObject
//import java.io.File
//
//class VolunteerPopupFragment : DialogFragment() {
//
//    private lateinit var sourceAddressTextView: TextView
//    private lateinit var destinationAddressTextView: TextView
//    private lateinit var deliverButton: Button
//
//    private lateinit var database: DatabaseReference
//    private var volunteerModel: VolunteerModel? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        database = FirebaseDatabase.getInstance().reference
//
//        // Retrieve passed VolunteerModel
//        arguments?.let {
//            volunteerModel = it.getParcelable(ARG_VOLUNTEER_MODEL)
//        }
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val view = inflater.inflate(R.layout.fragment_volunteer_popup, container, false)
//
//        sourceAddressTextView = view.findViewById(R.id.popup_source_address)
//        destinationAddressTextView = view.findViewById(R.id.popup_destination_address)
//        deliverButton = view.findViewById(R.id.deliver_button)
//
//        // Populate Source and Destination Addresses
//        volunteerModel?.let {
//            sourceAddressTextView.text = "Source Address: ${it.Saddress}"
//            destinationAddressTextView.text = "Destination Address: ${it.Daddress}"
//        }
//
//        // Handle Deliver Button Click
//        deliverButton.setOnClickListener {
//            volunteerModel?.let { model ->
//                assignDelivery(model)
//            }
//        }
//
//        return view
//    }
//
//    private fun assignDelivery(model: VolunteerModel) {
//        // Read volunteer data from local JSON
//        val userData = getUserDataFromLocal(requireContext())
//
//        if (userData != null) {
//            val volunteerName = userData["name"] ?: ""
//            val volunteerContact = userData["contactNumber"] ?: ""
//
//            if (volunteerName.isNotEmpty() && volunteerContact.isNotEmpty()) {
//                // Get the Firebase key for the specific donation node
//                val donationRef = database.child("Donations").child(model.key!!)
//
//                // Update the volunteer details and set Delivered to 1
//                val updates = mapOf(
//                    "Vname" to volunteerName,
//                    "Vcontact" to volunteerContact,
//                    "Delivered" to 1 // Mark as delivered
//                )
//
//                // Update the donation node
//                donationRef.updateChildren(updates).addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                        Toast.makeText(requireContext(), "Delivery assigned successfully!", Toast.LENGTH_SHORT).show()
//                        dismiss() // Close the popup after success
//                    } else {
//                        Toast.makeText(requireContext(), "Failed to assign delivery.", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            } else {
//                Toast.makeText(requireContext(), "Failed to retrieve volunteer details.", Toast.LENGTH_SHORT).show()
//            }
//        } else {
//            Toast.makeText(requireContext(), "User data file not found.", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    private fun getUserDataFromLocal(context: Context): Map<String, String>? {
//        val fileName = "user_data.json"
//        val file = File(context.filesDir, fileName)
//
//        return if (file.exists()) {
//            val jsonString = file.readText()
//            val jsonObject = JSONObject(jsonString)
//
//            mapOf(
//                "name" to jsonObject.optString("name", ""),
//                "contactNumber" to jsonObject.optString("contactNumber", "")
//            )
//        } else {
//            null
//        }
//    }
//
//    companion object {
//        private const val ARG_VOLUNTEER_MODEL = "volunteer_model"
//
//        fun newInstance(model: VolunteerModel): VolunteerPopupFragment {
//            val fragment = VolunteerPopupFragment()
//            val args = Bundle().apply {
//                putParcelable(ARG_VOLUNTEER_MODEL, model)
//            }
//            fragment.arguments = args
//            return fragment
//        }
//    }
//}
