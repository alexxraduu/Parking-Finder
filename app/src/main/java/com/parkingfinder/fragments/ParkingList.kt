package com.parkingfinder.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.parkingfinder.R
import com.parkingfinder.interfaces.ActivityFragmentCommunication


class ParkingList : Fragment() {
    var activityFragmentCommunication: ActivityFragmentCommunication? = null
    var tv: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_parking_list, container, false)
        tv=view.findViewById(R.id.test)
        return view
    }

    companion object {
        fun newInstance() = ParkingList()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv?.text=Firebase.auth.currentUser?.email.toString()
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ActivityFragmentCommunication) {
            activityFragmentCommunication = context
        }
    }
}