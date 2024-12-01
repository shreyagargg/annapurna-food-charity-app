package com.example.annpurna

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import com.google.firebase.database.*

class HistoryFragment : Fragment() {

    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_history, container, false)
        val historyTable = view.findViewById<TableLayout>(R.id.history_table)

        database = FirebaseDatabase.getInstance().reference.child("transaction")

        loadHistory(historyTable)

        return view
    }

    private fun loadHistory(historyTable: TableLayout) {
        database.orderByKey().limitToLast(5).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children) {
                    val row = TableRow(context)
                    val donor = TextView(context).apply { text = data.child("Dname").value.toString() }
                    val acceptor = TextView(context).apply { text = data.child("Rname").value.toString() }
                    val volunteer = TextView(context).apply { text = data.child("Vname").value.toString() }
                    val foodItem = TextView(context).apply { text = data.child("foodItem").value.toString() }
                    val quantity = TextView(context).apply { text = data.child("quantity").value.toString() }
                    val role = TextView(context).apply {
                        val username = getUserName()
                        text = when {
                            username == data.child("Dname").value.toString() -> "Donation"
                            username == data.child("Rname").value.toString() -> "Receiving"
                            username == data.child("Vname").value.toString() -> "Delivery"
                            else -> "Unknown"
                        }
                    }
                    row.addView(donor)
                    row.addView(acceptor)
                    row.addView(volunteer)
                    row.addView(foodItem)
                    row.addView(quantity)
                    row.addView(role)
                    historyTable.addView(row)
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun getUserName(): String {
        val sharedPreferences =
            requireActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE)
        return sharedPreferences.getString("savedText1", "Default Name")!!
    }
}
