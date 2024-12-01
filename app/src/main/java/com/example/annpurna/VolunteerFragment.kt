package com.example.annpurna

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class VolunteerFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var volunteerList: ArrayList<VolunteerModel>
    private lateinit var volunteerAdapter: VolunteerAdapter
    private lateinit var databaseReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = inflater.inflate(R.layout.fragment_volunteer, container, false)

        // Initialize RecyclerView
        recyclerView = binding.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        volunteerList = ArrayList()
        volunteerAdapter = VolunteerAdapter(volunteerList)
        recyclerView.adapter = volunteerAdapter

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().reference

        // Fetch the data
        fetchVolunteerData()

        return binding
    }

    private fun fetchVolunteerData() {
        // Fetch donations that need to be delivered (status: Delivered = 0)
        databaseReference.child("Donations").orderByChild("Delivered").equalTo("0")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (donationSnapshot in snapshot.children) {
                        // Get donor and receiver details from the transaction
                        val donorName = donationSnapshot.child("Dname").value.toString()
                        val donorContact = donationSnapshot.child("DContact").value.toString()
                        val receiverName = donationSnapshot.child("Rname").value.toString()
                        val receiverContact = donationSnapshot.child("Rcontact").value.toString()

                        // Fetch source address for donor from user_info database
                        fetchSourceAddress(donorName, donorContact, receiverName, receiverContact)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle possible errors here
                }
            })
    }

    private fun fetchSourceAddress(
        donorName: String, donorContact: String, receiverName: String, receiverContact: String
    ) {
        // Fetch source address from user_info database using donor's name and contact
        databaseReference.child("user_info").orderByChild("username")
            .equalTo(donorName).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        // Assuming there is a matching entry for the donor
                        val donorData = snapshot.children.first()
                        val sourceAddress = donorData.child("address").value.toString()

                        // Fetch destination address from user_info using receiver's name and contact
                        fetchDestinationAddress(receiverName, receiverContact, sourceAddress)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle possible errors here
                }
            })
    }

    private fun fetchDestinationAddress(
        receiverName: String, receiverContact: String, sourceAddress: String
    ) {
        // Fetch destination address from user_info database using receiver's name and contact
        databaseReference.child("user_info").orderByChild("username")
            .equalTo(receiverName).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        // Assuming there is a matching entry for the receiver
                        val receiverData = snapshot.children.first()
                        val destinationAddress = receiverData.child("address").value.toString()

                        // Now add the data to the volunteer list
                        val volunteer = VolunteerModel(
                            donorName = receiverName,
                            donorContact = receiverContact,
                            sourceAddress = sourceAddress,
                            receiverName = receiverName,
                            receiverContact = receiverContact,
                            destinationAddress = destinationAddress
                        )

                        volunteerList.add(volunteer)
                        volunteerAdapter.notifyDataSetChanged() // Notify adapter for changes
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle possible errors here
                }
            })
    }
}
