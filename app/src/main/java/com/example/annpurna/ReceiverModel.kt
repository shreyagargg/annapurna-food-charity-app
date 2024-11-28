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

class ReceiverAdapter(private val receiverList: ArrayList<ReceiverModel>, param: (Any) -> Unit) :
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

            // Navigate to PopupModelFragment
            view.findNavController().navigate(R.id.popupModelFragment, bundle)

        }
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


//class ItemAdapter(
//    private val itemList: List<Item>,
//    private val onItemClicked: (Item) -> Unit // Pass item details to callback
//) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {
//
//    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val itemName: TextView = itemView.findViewById(R.id.item_name)
//        // Add more views as needed
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
//        return ItemViewHolder(view)
//    }

//    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
//        val item = itemList[position]
//        holder.itemName.text = item.name
//        // Bind other data as needed
//
//        // Set click listener
//        holder.itemView.setOnClickListener {
//            onItemClicked(item) // Trigger the callback
//        }
//    }

//    override fun getItemCount(): Int = itemList.size
//}
