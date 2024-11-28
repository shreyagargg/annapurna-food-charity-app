package com.example.annpurna

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class VolunteerModel (
    var src: String = "",
    var dest: String = ""
)

class VolunteerAdapter(private val volunteerList: ArrayList<VolunteerModel>) :
    RecyclerView.Adapter<VolunteerAdapter.RecieverViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecieverViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_vol, parent, false)
        return RecieverViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecieverViewHolder, position: Int) {
        val volunteer = volunteerList[position]

        holder.source.setText(volunteer.src)
        holder.dest.setText(volunteer.dest)
    }

    override fun getItemCount(): Int {
        return volunteerList.size
    }

    inner class RecieverViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val source: TextView = view.findViewById(R.id.src)
        val dest: TextView = view.findViewById(R.id.dest)
    }
}