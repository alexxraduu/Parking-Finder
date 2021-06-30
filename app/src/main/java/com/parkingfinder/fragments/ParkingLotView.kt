package com.parkingfinder.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.parkingfinder.R
import com.parkingfinder.helper.LocationOperations
import com.parkingfinder.interfaces.ActivityFragmentCommunication
import com.parkingfinder.models.ParkingLot


class ParkingLotView(private val parkingLot: ParkingLot) : Fragment(), OnMapReadyCallback {
    private var activityFragmentCommunication: ActivityFragmentCommunication? = null
    lateinit var id: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        id = ""
    }

    @SuppressLint("ResourceType", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_parking_lot, container, false)


        val toolbar: Toolbar = view.findViewById(R.id.toolbar_back)
        toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        val tvAddress: TextView = view.findViewById(R.id.tv_view_address)
        val tvDescription: TextView = view.findViewById(R.id.tv_view_description)
        val tvIsPrivate: TextView = view.findViewById(R.id.tv_view_private)
        val tvNavigate: TextView = view.findViewById(R.id.tv_view_navigate)
        val tvReport: TextView = view.findViewById(R.id.tv_view_report)
        val btnEdit: Button = view.findViewById(R.id.tv_view_edit)
        btnEdit.setOnClickListener {
            Toast.makeText(context, "Not implemented yet!", Toast.LENGTH_SHORT).show()
        }
        tvReport.text =
            "${context?.getString(R.string.report_not_existing)} (${parkingLot.reports.toString()})"
        getDocId()
        tvReport.setOnClickListener {
            parkingLot.reports = parkingLot.reports!! + 1
            if (parkingLot.reports == 15) {
                deleteParkingLotFromDB()
            } else {
                tvReport.text =
                    "${context?.getString(R.string.report_not_existing)} (${parkingLot.reports.toString()})"
                updateDoc()
            }
        }

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

    private fun updateDoc() {
        val db = FirebaseFirestore.getInstance()
        db.collection("parking-lot")
            .document(id)

            .update("reported-as-not-existing", parkingLot.reports)
            .addOnSuccessListener { Log.d("FIREBASE", "DocumentSnapshot successfully updated!") }
            .addOnFailureListener { e -> Log.w("FIREBASE", "Error updating document", e) }
    }

    private fun deleteParkingLotFromDB() {
        val db = FirebaseFirestore.getInstance()
        db.collection("parking-lot").document(id)
            .delete()
            .addOnSuccessListener { Log.d("FIREBASE", "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w("FIREBASE", "Error deleting document", e) }
        requireActivity().supportFragmentManager.popBackStack()
    }

    private fun getDocId() {
        val db = FirebaseFirestore.getInstance()
        db.collection("parking-lot")
            .whereEqualTo("coordinates", parkingLot.coordinates)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    id = document.id
                }
            }
            .addOnFailureListener { exception ->
                Log.w("FIREBASE", "Error getting documents:", exception)
            }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ActivityFragmentCommunication) {
            activityFragmentCommunication = context
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val coordinates =
            LatLng(parkingLot.coordinates!!.latitude, parkingLot.coordinates!!.longitude)
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
