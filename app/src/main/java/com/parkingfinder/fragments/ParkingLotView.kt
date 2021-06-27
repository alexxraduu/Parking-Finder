package com.parkingfinder.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.location.*
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.auth.User
import com.parkingfinder.R
import com.parkingfinder.interfaces.ActivityFragmentCommunication
import com.parkingfinder.models.ParkingLot
import org.w3c.dom.Text
import java.util.*


class ParkingLotView(val parkingLot: ParkingLot) : Fragment() {

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
        val tv_address: TextView = view.findViewById(R.id.tv_view_address)
        val tv_description: TextView = view.findViewById(R.id.tv_view_description)
        val tv_isPrivate: TextView = view.findViewById(R.id.tv_view_private)

        tv_address.text = parkingLot.address
        tv_description.text = parkingLot.description

        when (parkingLot.isPrivate) {
            true -> tv_isPrivate.text = view.resources.getString(R.string.private_lot)
            false -> tv_isPrivate.text = view.resources.getString(R.string.public_lot)
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
}
