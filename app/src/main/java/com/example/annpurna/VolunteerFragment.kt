package com.example.annpurna

import android.os.Bundle
import android.util.Log
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

class VolunteerFragment : Fragment() {

    private lateinit var database: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var volunteerAdapter: VolunteerAdapter
    private val volunteerList: ArrayList<VolunteerModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FirebaseDatabase.getInstance().reference
        fetchVolunteerData()
    }

    private fun fetchVolunteerData() {
        val donationsRef = database.child("Donations")
        val userFile = File(requireContext().filesDir, "user_data.json")
        val currentCity: String? = if (userFile.exists()) {
            val userJson = JSONObject(userFile.readText())
            userJson.optString("city", null)
        } else {
            null
        }

        if (currentCity == null) {
            Toast.makeText(requireContext(), "City not found in local data.", Toast.LENGTH_SHORT).show()
            return
        }

        donationsRef.orderByChild("dcity").equalTo(currentCity)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(donationSnapshot: DataSnapshot) {
                    volunteerList.clear()

                    if (donationSnapshot.exists()) {
                        donationSnapshot.children.forEach { donationNode ->
                            val donation = donationNode.getValue(VolunteerModel::class.java)
                            if (donation != null && donation.accepted == 1) {
                                // Directly using the Daddress and Raddress from Firebase
                                donation.Saddress = donation.Daddress
                                donation.Daddress = donation.Raddress

                                // Add donation to the list if both source and destination addresses are valid
                                if (!donation.Saddress.isNullOrEmpty() && !donation.Daddress.isNullOrEmpty()) {
                                    volunteerList.add(donation)
                                }
                            }
                        }
                        volunteerAdapter.notifyDataSetChanged()
                    } else {
                        Toast.makeText(requireContext(), "No donations found for your city.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("VolunteerFragment", "Firebase error: ${error.message}")
                    Toast.makeText(requireContext(), "Failed to fetch data: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_volunteer, container, false)

        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        volunteerAdapter = VolunteerAdapter(volunteerList) { donation ->
            openVolunteerPopup(donation)
        }

        recyclerView.adapter = volunteerAdapter
        return view
    }

    private fun openVolunteerPopup(donation: VolunteerModel) {
        val popupFragment = VolunteerPopupFragment.newInstance(donation)
        popupFragment.show(childFragmentManager, "VolunteerPopupFragment")
    }

}
