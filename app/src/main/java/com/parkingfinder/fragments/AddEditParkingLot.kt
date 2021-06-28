package com.parkingfinder.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.ktx.Firebase
import com.parkingfinder.R
import com.parkingfinder.helper.LocationOperations
import com.parkingfinder.interfaces.ActivityFragmentCommunication
import com.parkingfinder.models.ParkingLot
import java.io.IOException


class AddEditParkingLot() : Fragment(), OnMapReadyCallback {
    lateinit var parkingLot: ParkingLot
    lateinit var coordinates: GeoPoint
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    var activityFragmentCommunication: ActivityFragmentCommunication? = null
    var tvAddress: TextView? = null
    var privateBtn: RadioButton? = null
    var publicBtn: RadioButton? = null
    var btnLocate: Button? = null
    var btnAdd: Button? = null
    var editDescription: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
    }

    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_add_edit_parking_lot, container, false)
        tvAddress = view.findViewById(R.id.tv_add_address)
        privateBtn = view.findViewById(R.id.radio_private)
        publicBtn = view.findViewById(R.id.radio_public)
        btnLocate = view.findViewById(R.id.btn_locate_me)
        btnAdd = view.findViewById(R.id.btn_add_parking_fg)
        editDescription = view.findViewById(R.id.edit_description)
        locate()

        // MAP
        val mapFragment = SupportMapFragment.newInstance()
        activity?.supportFragmentManager
            ?.beginTransaction()
            ?.add(R.id.frame_layout_map_add, mapFragment)
            ?.commit()
        mapFragment.getMapAsync(this)

        btnLocate!!.setOnClickListener {
            locate()
            mapFragment.getMapAsync(this)
        }

        btnAdd!!.setOnClickListener {
            if (createParkingLotObject()) {
                saveObjectToDb()
            }
        }

        return view
    }

    private fun saveObjectToDb() {
        val parkingLotData = hashMapOf(
            "UID" to parkingLot!!.UID,
            "coordinates" to parkingLot!!.coordinates,
            "description" to parkingLot!!.description,
            "locality" to parkingLot!!.locality!!.toLowerCase(),
            "private" to parkingLot!!.isPrivate,
            "reported-as-not-existing" to 0
        )
        val db = FirebaseFirestore.getInstance()
        db.collection("parking-lot").document()
            .set(parkingLotData)
            .addOnSuccessListener {
                Log.d("Add", "DocumentSnapshot successfully written!")
                Toast.makeText(context, "Thank you for adding a parking lot!", Toast.LENGTH_LONG)
                activity?.finish()
            }
            .addOnFailureListener { e ->
                Log.w("Add", "Error writing document", e)
                Toast.makeText(context, "An error occurred!", Toast.LENGTH_SHORT)
            }
    }

    @SuppressLint("MissingPermission")
    fun locate() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                coordinates = GeoPoint(location!!.latitude, location.longitude)
                tvAddress!!.text = LocationOperations.getAddress(coordinates, context)
            }
    }

    fun createParkingLotObject(): Boolean {
        if (editDescription!!.text.isNullOrBlank()) {
            editDescription!!.error = "Required field!"
            editDescription!!.requestFocus()
        } else if (!privateBtn!!.isChecked && !publicBtn!!.isChecked) {

            Toast.makeText(context, "Pick one. Parking must be public/private!", Toast.LENGTH_SHORT)
                .show()

        } else {
            parkingLot = ParkingLot(
                Firebase.auth.currentUser!!.uid,
                LocationOperations.getLocality(coordinates, context),
                coordinates,
                null,
                editDescription!!.text.toString(),
                privateBtn!!.isChecked

            )
            return true
        }
        return false
    }

    companion object {
        fun newInstance(): AddEditParkingLot {
            val args = Bundle()
            val fragment = AddEditParkingLot()
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
        val coordinatesLatLng = LatLng(coordinates!!.latitude, coordinates!!.longitude)
        val cameraPosition = CameraPosition.Builder()
            .target(coordinatesLatLng)
            .zoom(17f)
            .build()
        googleMap.clear()
        googleMap.addMarker(
            MarkerOptions()
                .position(coordinatesLatLng)
                .draggable(true)
        )
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        googleMap.setOnMarkerDragListener(object : OnMarkerDragListener {
            override fun onMarkerDragStart(marker: Marker) {}
            override fun onMarkerDrag(marker: Marker) {}
            override fun onMarkerDragEnd(marker: Marker) {
                val latLng = marker.position
                val geoPoint = GeoPoint(latLng.latitude, latLng.longitude)
                try {
                    coordinates = geoPoint
                    tvAddress!!.text = LocationOperations.getAddress(
                        geoPoint,
                        context
                    )

                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        })
    }
}
