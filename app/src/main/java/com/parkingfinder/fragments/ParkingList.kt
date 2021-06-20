package com.parkingfinder.fragments

import android.content.Context
import android.content.Intent
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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.parkingfinder.R
import com.parkingfinder.activities.MainActivity
import com.parkingfinder.interfaces.ActivityFragmentCommunication


class ParkingList : Fragment() {
    var activityFragmentCommunication: ActivityFragmentCommunication? = null
    var btn: Button? = null
    var toolbar: Toolbar? = null

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
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_toolbar, menu)
        val searchItem = menu!!.findItem(R.id.search)
        val searchView: SearchView = MenuItemCompat.getActionView(searchItem) as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                toolbar?.title = searchView.query.toString()
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
