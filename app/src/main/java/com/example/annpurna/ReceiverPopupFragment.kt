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

        // Update the Accepted field to 1 in the database
        val donationRef = database.child("Donations").orderByChild("foodItem")
            .equalTo(donation.foodItem) // You can change this query to a more suitable one, like using a unique ID

        donationRef.get().addOnSuccessListener { snapshot ->
            snapshot.children.forEach {
                val donationKey = it.key
                if (donationKey != null) {
                    // Update the donation's accepted status to 1
                    database.child("Donations").child(donationKey).child("accepted").setValue(1)
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
