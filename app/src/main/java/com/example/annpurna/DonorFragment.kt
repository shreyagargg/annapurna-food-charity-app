package com.example.annpurna

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

class DonorFragment : Fragment(), OnMapReadyCallback {

//    private var param1: String? = null
//    private var param2: String? = null
    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_donor, container, false)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val item: EditText = view.findViewById(R.id.foodItem)
        val desc: EditText = view.findViewById(R.id.foodDescrip)
        val date: EditText = view.findViewById(R.id.date)
        val button: Button = view.findViewById(R.id.donate)

        button.setOnClickListener {
            val foodItem = item.text.toString()
            val description = desc.text.toString()
            val dateExp = date.text.toString()
            if (foodItem.isNotBlank() && description.isNotBlank() && dateExp.isNotBlank()) {
                Toast.makeText(requireContext(), "Donation of $foodItem Successful", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Please fill all columns", Toast.LENGTH_SHORT).show()
            }
        }

        return view


    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val delhi = LatLng(28.613, 77.209)
        mMap.addMarker(MarkerOptions().position(delhi).title("Marker in Delhi"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(delhi))
    }

}
