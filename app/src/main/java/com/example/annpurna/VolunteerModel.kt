package com.example.annpurna

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


data class VolunteerModel(
    var donorName: String = "",
    var donorContact: String = "",
    var sourceAddress: String = "",
    var receiverName: String = "",
    var receiverContact: String = "",
    var destinationAddress: String = ""
)


class VolunteerAdapter(private val volunteerList: ArrayList<VolunteerModel>) :
    RecyclerView.Adapter<VolunteerAdapter.VolunteerViewHolder>() {

    // Create view holder for each item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VolunteerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_vol, parent, false) // Layout for individual item
        return VolunteerViewHolder(view)
    }

    // Bind data to the ViewHolder (each RecyclerView item)
    override fun onBindViewHolder(holder: VolunteerViewHolder, position: Int) {
        val volunteer = volunteerList[position]

        // Set the source and destination addresses in the corresponding TextViews
        holder.source.text = volunteer.sourceAddress
        holder.dest.text = volunteer.destinationAddress
    }

    // Return the total number of items
    override fun getItemCount(): Int {
        return volunteerList.size
    }

    // ViewHolder to hold references to the TextViews for each item
    inner class VolunteerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val source: TextView = view.findViewById(R.id.src)    // Source address TextView
        val dest: TextView = view.findViewById(R.id.dest)      // Destination address TextView
    }
}
