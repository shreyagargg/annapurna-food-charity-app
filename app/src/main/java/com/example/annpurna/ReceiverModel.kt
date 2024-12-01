package com.example.annpurna

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import java.io.Serializable

// Receiver Model
// Receiver Model
data class ReceiverModel(
    var foodItem: String = "",          // Name of the food item
    var expiryDate: String = "",       // Expiry date of the item
    var quantity: String = "",         // Quantity of the item
    var quantityType: String = "",     // Type of quantity (e.g., kg, litres, pieces)
    var description: String = "",      // Additional description of the food
    var Dname: String = "",            // Name of the donor
    var Dcontact: String = "",         // Contact details of the donor
    var city: String = "",             // City for filtering by location
    var accepted: Int = 0,             // Status: 0 = not accepted, 1 = taken, -1 = expired
    var Rname: String = "",            // Name of the receiver (new field)
    var Rcontact: String = ""          // Contact details of the receiver (new field)
) : Serializable


// Receiver Adapter
class ReceiverAdapter(
    private val receiverList: ArrayList<ReceiverModel>,
    private val onItemClicked: (ReceiverModel) -> Unit
) : RecyclerView.Adapter<ReceiverAdapter.ReceiverViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceiverViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_receiver, parent, false)
        return ReceiverViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReceiverViewHolder, position: Int) {
        val receiver = receiverList[position]

        // Set the data into the UI components
        holder.itemName.text = receiver.foodItem
        holder.itemDetails.text = """
            Expiry Date: ${receiver.expiryDate}
            Quantity: ${receiver.quantity} ${receiver.quantityType}
        """.trimIndent()

        holder.itemView.setOnClickListener {
            onItemClicked(receiver) // Trigger the click listener
        }
    }

    override fun getItemCount(): Int = receiverList.size

    // ViewHolder for Receiver Items
    inner class ReceiverViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemName: TextView = view.findViewById(R.id.item_name)
        val itemDetails: TextView = view.findViewById(R.id.item_details)
    }
}
