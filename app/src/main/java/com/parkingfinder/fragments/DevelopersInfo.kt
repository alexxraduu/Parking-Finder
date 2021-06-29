package com.parkingfinder.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.parkingfinder.R
import com.parkingfinder.helper.LocationOperations
import com.parkingfinder.interfaces.ActivityFragmentCommunication
import com.parkingfinder.models.ParkingLot


class DevelopersInfo(private val info: String) : Fragment() {

    private var activityFragmentCommunication: ActivityFragmentCommunication? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_developers_info, container, false)

        val tvInfo: TextView= view.findViewById(R.id.tv_info)
        tvInfo.text=info


        return view
    }


    companion object {
        fun newInstance(info: String): DevelopersInfo {
            val args = Bundle()
            val fragment = DevelopersInfo(info)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ActivityFragmentCommunication) {
            activityFragmentCommunication = context
        }
    }


}
