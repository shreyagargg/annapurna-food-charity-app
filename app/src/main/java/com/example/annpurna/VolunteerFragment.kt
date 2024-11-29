package com.example.annpurna

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class VolunteerFragment : Fragment() {

    private lateinit var database: DatabaseReference

    private lateinit var recyclerView: RecyclerView
    private lateinit var volAdapter: VolunteerAdapter
    private var volunteerList: ArrayList<VolunteerModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FirebaseDatabase.getInstance().reference
        fetchData()
    }


    private fun fetchData() {
        val donationsRef = database.child("Donations")

        // Attach a listener to read the data
        donationsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // Parse the data
                    for (userSnapshot in snapshot.children) {
                        val user = userSnapshot.getValue(ReceiverModel::class.java)
                        if (user != null) {
                            volunteerList.add(VolunteerModel("User: ${user.foodItem}", "Age: ${user.foodItem}"))
                            volAdapter.notifyItemInserted(volunteerList.size - 1)
                            // Empty fields for the new form

                            // Display fetched data
//                            Toast.makeText(requireContext(), "User: ${user.name}, Age: ${user.description}", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), "No data available", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
                Toast.makeText(requireContext(), "Failed to read value.", Toast.LENGTH_SHORT).show()
            }
        })
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_receiver, container, false)

        recyclerView = view.findViewById(R.id.item_list)
//        val name = view.findViewById<TextView>(R.id.name)
//        val desc = view.findViewById<TextView>(R.id.name)
//        addMoreButton = view.findViewById(R.id.addMore)
//        donateButton = view.findViewById(R.id.donate)

        // Set up RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)
        volAdapter = VolunteerAdapter(volunteerList)
        recyclerView.adapter = volAdapter

        volunteerList.add(VolunteerModel("one", "two"))
        volunteerList.add(VolunteerModel("one", "two"))
        volunteerList.add(VolunteerModel("one", "two")) // Empty fields for the new form
//        recieverList.add(ReceiverModel("one", "two")) // Empty fields for the new form
//        recieverList.add(ReceiverModel("one", "two")) // Empty fields for the new form
        volAdapter.notifyItemInserted(volunteerList.size - 1)
        Toast.makeText(requireContext(), "Accept", Toast.LENGTH_SHORT).show()



        return view
    }


}