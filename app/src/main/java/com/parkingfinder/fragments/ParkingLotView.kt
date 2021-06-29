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


class ParkingLotView(private val parkingLot: ParkingLot) : Fragment(), OnMapReadyCallback {

    var activityFragmentCommunication: ActivityFragmentCommunication? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_parking_lot, container, false)


        val toolbar: Toolbar = view.findViewById(R.id.toolbar_back);
        toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        val tvAddress: TextView = view.findViewById(R.id.tv_view_address)
        val tvDescription: TextView = view.findViewById(R.id.tv_view_description)
        val tvIsPrivate: TextView = view.findViewById(R.id.tv_view_private)
        val tvNavigate: TextView = view.findViewById(R.id.tv_view_navigate)

        tvAddress.text = parkingLot.address
        tvDescription.text = parkingLot.description

        when (parkingLot.isPrivate) {
            true -> tvIsPrivate.text = view.resources.getString(R.string.private_lot)
            false -> tvIsPrivate.text = view.resources.getString(R.string.public_lot)
        }

        // MAP
        val mapFragment = SupportMapFragment.newInstance()
        activity?.supportFragmentManager
            ?.beginTransaction()
            ?.add(R.id.frame_layout_map, mapFragment)
            ?.commit()
        mapFragment.getMapAsync(this)

        tvNavigate.setOnClickListener {
            LocationOperations.openGoogleMaps(parkingLot.coordinates!!, view.context)
        }
        return view
    }


    companion object {
        fun newInstance(parkingLot: ParkingLot): ParkingLotView {
            val args = Bundle()
            val fragment = ParkingLotView(parkingLot)
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

    override fun onMapReady(googleMap: GoogleMap) {
        val coordinates =
            LatLng(parkingLot.coordinates!!.latitude, parkingLot!!.coordinates!!.longitude)
        val cameraPosition = CameraPosition.Builder()
            .target(coordinates)
            .zoom(15f)
            .build()

        googleMap.addMarker(
            MarkerOptions()
                .position(coordinates)
                .title(parkingLot.address)
        )
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }
}
