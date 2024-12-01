package com.example.annpurna

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import org.json.JSONObject
import java.io.File

class ReceiverFragment : Fragment() {

    private lateinit var database: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var receiverAdapter: ReceiverAdapter
    private var receiverList: ArrayList<ReceiverModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FirebaseDatabase.getInstance().reference
        fetchData()
    }

    private fun fetchData() {
        val donationsRef = database.child("Donations")
        val userFile = File(requireContext().filesDir, "user_data.json")
        var currentUser: JSONObject? = null

        if (userFile.exists()) {
            currentUser = JSONObject(userFile.readText())
        }

        donationsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                receiverList.clear()
                if (snapshot.exists()) {
                    for (donationSnapshot in snapshot.children) {
                        val donation = donationSnapshot.getValue(ReceiverModel::class.java)
                        if (donation != null) {
                            val accepted = donation.accepted
                            val donorName = donation.Dname
                            val donorContact = donation.Dcontact

                            // Conditions: Skip items if Accepted = 1/-1 or user is the donor
                            if (accepted != 1 && accepted != -1) {
                                if (currentUser == null ||
                                    donorName != currentUser.optString("name") ||
                                    donorContact != currentUser.optString("contactNumber")
                                ) {
                                    receiverList.add(donation)
                                }
                            }
                        }
                    }
                    receiverAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(requireContext(), "No data available", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Failed to read data.", Toast.LENGTH_SHORT).show()
            }
        })
    }
    fun refreshReceiverList() {
        // Refresh the list after an item is accepted
        fetchData() // You can also implement specific logic to just remove the accepted item
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_receiver, container, false)

        recyclerView = view.findViewById(R.id.item_list)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        receiverAdapter = ReceiverAdapter(receiverList) { donation ->
            openPopup(donation)
        }
        recyclerView.adapter = receiverAdapter

        return view
    }

    private fun openPopup(donation: ReceiverModel) {
        val popupFragment = ReceiverPopupFragment.newInstance(donation)
        popupFragment.show(childFragmentManager, "ReceiverPopupFragment")
    }
}
