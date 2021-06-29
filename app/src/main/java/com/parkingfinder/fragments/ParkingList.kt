package com.parkingfinder.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.*
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuItemCompat
import androidx.core.view.isEmpty
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.ktx.Firebase
import com.parkingfinder.R
import com.parkingfinder.activities.MainActivity
import com.parkingfinder.activities.ThirdActivity
import com.parkingfinder.adapters.ParkingLotAdapter
import com.parkingfinder.helper.LocationOperations
import com.parkingfinder.helper.LocationOperations.Companion.getAddress
import com.parkingfinder.helper.LocationOperations.Companion.getLocality
import com.parkingfinder.helper.LocationOperations.Companion.myLocation
import com.parkingfinder.helper.LocationOperations.Companion.openGoogleMaps
import com.parkingfinder.interfaces.ActivityFragmentCommunication
import com.parkingfinder.interfaces.OnItemClickedListener
import com.parkingfinder.models.ParkingLot
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class ParkingList : Fragment() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    var activityFragmentCommunication: ActivityFragmentCommunication? = null
    var infoText=""
    var toolbar: Toolbar? = null
    var parkingList: ArrayList<ParkingLot> = ArrayList<ParkingLot>()
    var parkingAdapter: ParkingLotAdapter = ParkingLotAdapter(parkingList,
        object : OnItemClickedListener {
            override fun openMaps(location: GeoPoint?) {
                openGoogleMaps(location!!, context)
            }

            override fun openParkingLotView(parkingLot: ParkingLot?) {
                activityFragmentCommunication!!.addParkingLotViewFragment(parkingLot)
                //Toast.makeText(context,parkingLot!!.reports.toString(),Toast.LENGTH_SHORT).show()
            }
        })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
        getLocation()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_parking_list, container, false)

        toolbar = view.findViewById<View>(R.id.toolbar) as Toolbar
        val activity = activity as AppCompatActivity?
        activity!!.setSupportActionBar(toolbar)
        setHasOptionsMenu(true)

        val recyclerView = view.findViewById<View>(R.id.rv_parking_lots) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = parkingAdapter

        val btn_add = view.findViewById<View>(R.id.btn_add_parking)
        btn_add.setOnClickListener {
            val intent = Intent(context, ThirdActivity::class.java)
            activity?.startActivity(intent)
        }
        return view
    }

    override fun onResume() {
        super.onResume()
        updateToolbarTitle()
        getLocation()
        parkingAdapter.notifyDataSetChanged()
    }

    fun updateToolbarTitle() {
        toolbar?.title = LocationOperations.searchedLocality!!.toUpperCase()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (menu.isEmpty()) {
            inflater.inflate(R.menu.menu_toolbar, menu)
        }
        val searchItem = menu.findItem(R.id.search)
        val searchView: SearchView = MenuItemCompat.getActionView(searchItem) as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                LocationOperations.searchedLocality = searchView.query.toString().toLowerCase()
                updateToolbarTitle()
                getDataExample()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        val logoutItem = menu!!.findItem(R.id.item_logout)
        logoutItem.setOnMenuItemClickListener {
            logout()
            false
        }

        var locate = menu!!.findItem(R.id.action_locate)
        locate.setOnMenuItemClickListener {
            getLocation()
            false
        }
        var about =  menu!!.findItem(R.id.item_about)
        about.setOnMenuItemClickListener {
            getDev()
            false
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun getDev(){
        val queue: RequestQueue = Volley.newRequestQueue(context)
        var url = "https://mocki.io/v1/cc9faba3-12c6-44ae-a9e0-8acd60a144ff"

        val stringRequest = StringRequest(
            Request.Method.GET,
            url,
            {
               response-> handleResponse(response)
            },
            { TODO("Not yet implemented") })
        queue.add(stringRequest)
    }

    private fun handleResponse(response: String?) {
        infoText=""
        try {
            val jsonArray = JSONArray(response)
            for (index in 0 until jsonArray.length()) {
                var obj: JSONObject = jsonArray.getJSONObject(index)
                var name = obj.getString("name")
                var email = obj.getString("email")
                var birthDate = obj.getString("birthdate")
                var faculty = obj.getString("faculty")

                infoText+="\n\nName: $name" +
                        "\nEmail: $email" +
                        "\nDate of birth: $birthDate" +
                        "\nFaculty: $faculty\n"

            }
            activityFragmentCommunication!!.addDevelopersInfoFragment(infoText)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }


    @SuppressLint("MissingPermission")
    private fun getLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                try {
                    myLocation =
                        GeoPoint(location!!.latitude, location.longitude)
                    showResults()
                } catch (exception: NullPointerException) {

                }
            }
    }

    private fun showResults() {
        LocationOperations.searchedLocality = getLocality(
            myLocation,
            context
        ).toLowerCase()
        getDataExample()
        updateToolbarTitle()
    }

    companion object {
        fun newInstance() = ParkingList()
    }

    private fun logout() {
        Firebase.auth.signOut()
        if (Firebase.auth.currentUser?.email.isNullOrEmpty()) {
            Toast.makeText(
                context,
                "Logged out successfully!",
                Toast.LENGTH_LONG
            )
                .show()
        }
        val intent = Intent(context, MainActivity::class.java)
        activity?.startActivity(intent)
        activity?.finish()
    }

    fun getDataExample() {
        val db = FirebaseFirestore.getInstance()
        db.collection("parking-lot")
            .whereEqualTo("locality", LocationOperations.searchedLocality)
            .get()
            .addOnSuccessListener { documents ->
                parkingList.clear()
                for (document in documents) {
                    parkingList.add(
                        ParkingLot(
                            document["UID"] as String,
                            document["locality"] as String,
                            document["coordinates"] as GeoPoint,
                            getAddress(document["coordinates"] as GeoPoint, context),
                            document["description"] as String,
                            document["private"] as Boolean,
                            (document["reported-as-not-existing"] as Number).toInt()
                        )
                    )
                }
                sortResults()
                parkingAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.w("FIREBASE", "Error getting documents:", exception)
            }
    }

    private fun distance(parkingLot: ParkingLot): Int {
        return LocationOperations.distanceBetweenGeoPoints(
            parkingLot.coordinates!!,
            myLocation
        )
    }

    private fun sortResults() {
        parkingList.sortWith { p1, p2 ->
            when {
                distance(p1) > distance(p2) -> 1
                distance(p1) == distance(p2) -> 0
                else -> -1
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateToolbarTitle()
        getLocation()
        parkingAdapter.notifyDataSetChanged()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ActivityFragmentCommunication) {
            activityFragmentCommunication = context
        }
    }
}

