package com.example.annpurna

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.json.JSONObject
import java.io.File

class ReceiverPopupFragment : DialogFragment() {

    private lateinit var itemName: TextView
    private lateinit var itemDetails: TextView
    private lateinit var acceptButton: Button
    private lateinit var itemImage: ImageView
    private lateinit var database: DatabaseReference

    companion object {
        private const val ARG_DONATION = "donation"

        fun newInstance(donation: ReceiverModel): ReceiverPopupFragment {
            val fragment = ReceiverPopupFragment()
            val bundle = Bundle()
            bundle.putSerializable(ARG_DONATION, donation)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_receiver_popup, container, false)

        itemName = view.findViewById(R.id.popup_item_name)
        itemDetails = view.findViewById(R.id.popup_item_details)
        acceptButton = view.findViewById(R.id.accept_button)
        itemImage = view.findViewById(R.id.popup_item_image)

        val donation = arguments?.getSerializable(ARG_DONATION) as? ReceiverModel
        if (donation != null) {
            displayDetails(donation)
        }

        acceptButton.setOnClickListener {
            if (donation != null) {
                acceptDonation(donation)
            }
        }

        return view
    }

    private fun displayDetails(donation: ReceiverModel) {
        itemName.text = donation.foodItem
        itemDetails.text = """
            Description: ${donation.description}
            Expiry Date: ${donation.expiryDate}
            Quantity: ${donation.quantity} ${donation.quantityType}
            Donor Name: ${donation.Dname}
            Donor Contact: ${donation.Dcontact}
        """.trimIndent()

        // Set image if available (placeholder if not)
        itemImage.setImageResource(R.drawable.donor_image) // Replace with actual image loading logic
    }

    private fun acceptDonation(donation: ReceiverModel) {
        // Initialize Firebase reference
        database = FirebaseDatabase.getInstance().reference

        // Retrieve the current user's details (rname, rcontact, and raddress) from local JSON file
        val userFile = File(requireContext().filesDir, "user_data.json")
        var currentUser: JSONObject? = null
        var rname = ""
        var rcontact = ""
        var raddress = ""

        if (userFile.exists()) {
            currentUser = JSONObject(userFile.readText())
            rname = currentUser.optString("name")  // Retrieve receiver's name
            rcontact = currentUser.optString("contactNumber")  // Retrieve receiver's contact number
            raddress = currentUser.optString("address")  // Retrieve receiver's address
        }

        // Update the Accepted field and add receiver's name, contact details, and address
        val donationRef = database.child("Donations").orderByChild("foodItem")
            .equalTo(donation.foodItem) // You can change this query to a more suitable one, like using a unique ID

        donationRef.get().addOnSuccessListener { snapshot ->
            snapshot.children.forEach { donationSnapshot ->
                val donationKey = donationSnapshot.key
                if (donationKey != null) {
                    // Update the donation's accepted status to 1
                    val donationUpdates = mapOf(
                        "accepted" to 1,
                        "rname" to rname,  // Set receiver's name
                        "rcontact" to rcontact,  // Set receiver's contact number
                        "raddress" to raddress  // Set receiver's address
                    )

                    database.child("Donations").child(donationKey).updateChildren(donationUpdates)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                dismiss() // Close the popup
                                refreshReceiverFragment() // Refresh the ReceiverFragment
                            }
                        }
                }
            }
        }
    }

    private fun refreshReceiverFragment() {
        val receiverFragment = parentFragment as? ReceiverFragment
        receiverFragment?.refreshReceiverList() // Refresh the data by calling fetchData in ReceiverFragment
    }
}
