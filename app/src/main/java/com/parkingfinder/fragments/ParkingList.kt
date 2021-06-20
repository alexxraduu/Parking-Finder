package com.parkingfinder.fragments

import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.ktx.Firebase
import com.parkingfinder.R
import com.parkingfinder.activities.MainActivity
import com.parkingfinder.adapters.ParkingLotAdapter
import com.parkingfinder.interfaces.ActivityFragmentCommunication
import com.parkingfinder.models.ParkingLot
import java.util.*


class ParkingList : Fragment() {
    var activityFragmentCommunication: ActivityFragmentCommunication? = null
    var btn: Button? = null
    var toolbar: Toolbar? = null
    var parkingList: ArrayList<ParkingLot> = ArrayList<ParkingLot>()
    var parkingAdapter: ParkingLotAdapter = ParkingLotAdapter(parkingList)
    var currentLocation: String ="Brasov"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_toolbar, menu)
        val searchItem = menu!!.findItem(R.id.search)
        val searchView: SearchView = MenuItemCompat.getActionView(searchItem) as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                toolbar?.title = searchView.query.toString()
                currentLocation= searchView.query.toString()
                getDataExample()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        val down = menu!!.findItem(R.id.item_logout)
        down.setOnMenuItemClickListener {
            logout()
            false
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    companion object {
        fun newInstance() = ParkingList()
    }

    private fun logout() {
        Firebase.auth.signOut()
        if (Firebase.auth.currentUser?.email.isNullOrEmpty()) {
            Toast.makeText(
                context,
                "Logged out",
                Toast.LENGTH_LONG
            )
                .show()
        }
        val intent = Intent(context, MainActivity::class.java)
        activity?.startActivity(intent)
        activity?.finish()
    }
    fun getDataExample(){

        val tag="testfirebase"
        val db= FirebaseFirestore.getInstance()
        db.collection("parking-lot")
            .whereEqualTo("city",currentLocation)
            .get()
            .addOnSuccessListener{documents->
                parkingList.clear()
                for(document in documents){
                    parkingList.add(ParkingLot(document["description"] as String, document["coordinates"] as GeoPoint))
                }
                parkingAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener{exception->
                Log.w(tag,"Error getting documents:",exception)
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
