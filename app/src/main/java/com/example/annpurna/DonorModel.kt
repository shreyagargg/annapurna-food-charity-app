package com.example.annpurna

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView

data class DonorModel(
    var name: String,
    var description: String,
    var date: String,
    val quantityType: String,
    var quantity: String,
//    val imageUrl: String
)

class DonorAdapter(private val donorList: ArrayList<DonorModel>) :
    RecyclerView.Adapter<DonorAdapter.DonorViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DonorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_donor, parent, false)
        return DonorViewHolder(view)
    }

    override fun onBindViewHolder(holder: DonorViewHolder, position: Int) {
        val donor = donorList[position]

        // Set the data into the UI components
        holder.foodItemEditText.setText(donor.name)
        holder.descEditText.setText(donor.description)
        holder.expEditText.setText(donor.date)
        holder.quantityEditText.setText(donor.quantity)

        // Add TextChangedListeners to update donor object when the user types something
        holder.foodItemEditText.addTextChangedListener { text ->
            donor.name = text.toString()
        }
        holder.descEditText.addTextChangedListener { text ->
            donor.description = text.toString()
        }
        holder.expEditText.addTextChangedListener { text ->
            donor.date = text.toString()
        }
        holder.quantityEditText.addTextChangedListener { text ->
            donor.quantity = text.toString()
        }
    }

    override fun getItemCount(): Int {
        return donorList.size
    }

    inner class DonorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val foodItemEditText: EditText = view.findViewById(R.id.name)
        val descEditText: EditText = view.findViewById(R.id.desc)
        val expEditText: EditText = view.findViewById(R.id.exp)
        val quantityEditText: EditText = view.findViewById(R.id.quantity)
    }
}
