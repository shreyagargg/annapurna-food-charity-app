//package com.example.annpurna
//
//import android.app.Dialog
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Button
//import android.widget.TextView
//import android.widget.Toast
//import androidx.fragment.app.Fragment
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.google.firebase.database.*
//
//class ReceiverFragment : Fragment() {
//
//    private lateinit var database: DatabaseReference
//
//    private lateinit var recyclerView: RecyclerView
//    private lateinit var recieverAdapter: ReceiverAdapter
//    private var recieverList: ArrayList<ReceiverModel> = ArrayList()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        database = FirebaseDatabase.getInstance().reference
//        fetchData()
//    }
//
//    private fun fetchData() {
//        val donationsRef = database.child("Donations")
//
//        donationsRef.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                if (snapshot.exists()) {
//                    // Parse the data
//                    for (userSnapshot in snapshot.children) {
//                        val user = userSnapshot.getValue(ReceiverModel::class.java)
//                        if (user != null) {
//                            recieverList.add(ReceiverModel("Item: ${user.foodItem}", "Expiry Date: ${user.date}"))
//                            recieverAdapter.notifyItemInserted(recieverList.size - 1)
//                        // Empty fields for the new form
//
//                            // Display fetched data
////                            Toast.makeText(requireContext(), "User: ${user.name}, Age: ${user.description}", Toast.LENGTH_SHORT).show()
//                        }
//                    }
//                } else {
//                    Toast.makeText(requireContext(), "No data available", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                // Handle error
//                Toast.makeText(requireContext(), "Failed to read value.", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val view = inflater.inflate(R.layout.fragment_receiver, container, false)
//
//        recyclerView = view.findViewById(R.id.item_list)
////        recyclerView.setOnClickListener {
//////            Toast.makeText(requireContext(), "clicked", Toast.LENGTH_SHORT).show()
////        }
////        val name = view.findViewById<TextView>(R.id.name)
////        val desc = view.findViewById<TextView>(R.id.name)
////        addMoreButton = view.findViewById(R.id.addMore)
////        donateButton = view.findViewById(R.id.donate)
//
//        // Set up RecyclerView
//        recyclerView.layoutManager = LinearLayoutManager(activity)
//        recieverAdapter = ReceiverAdapter(recieverList)
//        recyclerView.adapter = recieverAdapter
//
//        recieverAdapter.notifyItemInserted(recieverList.size - 1)
//        Toast.makeText(requireContext(), "Clicked:", Toast.LENGTH_SHORT).show()
//
//
//
//        return view
//    }
//}
//
package com.example.annpurna

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.*

class ReceiverFragment : Fragment() {

    private lateinit var database: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var recieverAdapter: ReceiverAdapter
    private var recieverList: ArrayList<ReceiverModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FirebaseDatabase.getInstance().reference
        fetchData()
    }

    private fun fetchData() {
        val donationsRef = database.child("Donations")

        donationsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {
                        val user = userSnapshot.getValue(ReceiverModel::class.java)
                        if (user != null) {
                            recieverList.add(
                                ReceiverModel(
                                    foodItem = user.foodItem,
                                    date = user.date
                                )
                            )
                            recieverAdapter.notifyItemInserted(recieverList.size - 1)
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), "No data available", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
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

        recyclerView.layoutManager = LinearLayoutManager(activity)
        recieverAdapter = ReceiverAdapter(recieverList) { item ->
            // Navigate to PopupModel fragment
            val action = PopupModelFragment.actionReceiverFragmentToPopupModelFragment(
                foodItem = "grapes",//item.foodItem,,
                expiryDate = "23"//item.foodItem,
                //"item.date"
            )
            findNavController().navigate(action)
        }
        recyclerView.adapter = recieverAdapter

        return view
    }
}
