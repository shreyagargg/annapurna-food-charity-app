package com.example.annpurna

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


data class ReceiverModel(
    var name: String = "",
    var description: String = ""
)

class ReceiverAdapter(private val receiverList: ArrayList<ReceiverModel>) :
RecyclerView.Adapter<ReceiverAdapter.RecieverViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecieverViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_card, parent, false)
        return RecieverViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecieverViewHolder, position: Int) {
        val receiver = receiverList[position]

        // Set the data into the UI components
        holder.itemname.setText(receiver.name)
        holder.desc.setText(receiver.description)
//        holder.expEditText.setText(donor.date)
//        holder.quantityEditText.setText(donor.quantity)

        // Add TextChangedListeners to update donor object when the user types something
//        holder.itemname.addTextChangedListener { text ->
//            reciever.name = text.toString()
//        }
//        holder.desc.addTextChangedListener { text ->
//            reciever.description = text.toString()
//        }
//        holder.expEditText.addTextChangedListener { text ->
//            donor.date = text.toString()
//        }
//        holder.quantityEditText.addTextChangedListener { text ->
//            donor.quantity = text.toString()
//        }
    }

    override fun getItemCount(): Int {
        return receiverList.size
    }

    inner class RecieverViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemname: TextView = view.findViewById(R.id.item_name)
        val desc: TextView = view.findViewById(R.id.item_details)
//        val expEditText: EditText = view.findViewById(R.id.exp)
//        val quantityEditText: EditText = view.findViewById(R.id.quantity)
    }
}