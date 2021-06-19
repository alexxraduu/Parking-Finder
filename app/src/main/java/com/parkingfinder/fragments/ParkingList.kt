package com.parkingfinder.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.parkingfinder.R
import com.parkingfinder.activities.MainActivity
import com.parkingfinder.activities.SecondActivity
import com.parkingfinder.interfaces.ActivityFragmentCommunication


class ParkingList : Fragment() {
    var activityFragmentCommunication: ActivityFragmentCommunication? = null
    var btn: Button?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_parking_list, container, false)
        btn=view.findViewById(R.id.btn_logout)
        return view
    }

    companion object {
        fun newInstance() = ParkingList()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn?.setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        Firebase.auth.signOut()
        if(Firebase.auth.currentUser?.email.isNullOrEmpty()){
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ActivityFragmentCommunication) {
            activityFragmentCommunication = context
        }
    }
}