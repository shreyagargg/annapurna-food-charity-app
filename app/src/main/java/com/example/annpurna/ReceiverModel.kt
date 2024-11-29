package com.example.annpurna

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView


data class ReceiverModel(
    var foodItem: String = "",
    var date: String = ""
)

class ReceiverAdapter(private val receiverList: ArrayList<ReceiverModel>) :
RecyclerView.Adapter<ReceiverAdapter.RecieverViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecieverViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_receiver, parent, false)
        return RecieverViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecieverViewHolder, position: Int) {
        val receiver = receiverList[position]

        // Set the data into the UI components
        holder.itemname.setText(receiver.foodItem)
        holder.desc.setText(receiver.date)

        holder.itemView.setOnClickListener {
            Toast.makeText(holder.itemView.context, "Clicked:}", Toast.LENGTH_SHORT).show()
            val bundle = Bundle().apply {
                putString("foodItem", receiver.foodItem)
                putString("expiryDate", receiver.date)
            }

        }
    }

    override fun getItemCount(): Int {
        return receiverList.size
    }

    inner class RecieverViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemname: TextView = view.findViewById(R.id.item_name)
        val desc: TextView = view.findViewById(R.id.item_details)
    }
}
