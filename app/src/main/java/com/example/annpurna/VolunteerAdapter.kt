package com.example.annpurna

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class VolunteerAdapter(
    private val volunteerList: List<VolunteerModel>,
    private val onClick: (VolunteerModel) -> Unit
) : RecyclerView.Adapter<VolunteerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_vol, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val volunteer = volunteerList[position]

        // Bind data to the TextViews
        holder.sourceAddressTextView.text = volunteer.Saddress ?: "No source address available"
        holder.destinationAddressTextView.text = volunteer.Daddress ?: "No destination address available"

        // Handle item clicks
        holder.itemView.setOnClickListener {
            onClick(volunteer)
        }
    }

    override fun getItemCount(): Int = volunteerList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sourceAddressTextView: TextView = itemView.findViewById(R.id.src)
        val destinationAddressTextView: TextView = itemView.findViewById(R.id.dest)
    }
}
