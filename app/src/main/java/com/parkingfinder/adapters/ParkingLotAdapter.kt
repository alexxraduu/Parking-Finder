package com.parkingfinder.adapters

import android.app.Application
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.parkingfinder.R
import com.parkingfinder.activities.SecondActivity
import com.parkingfinder.models.ParkingLot
import java.util.*

class ParkingLotAdapter(private val parkingLots: ArrayList<ParkingLot>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_parking_lot, parent, false)
        return ParkingLotViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val parkingLot = parkingLots[position]
        (holder as ParkingLotViewHolder).bind(parkingLot)
    }

    override fun getItemCount(): Int {
        return parkingLots.size
    }

    internal inner class ParkingLotViewHolder(private val view: View) : RecyclerView.ViewHolder(
        view
    ) {

        private val description: TextView
        private val coordinates: TextView
        fun bind(parkingLot: ParkingLot) {
            val geoCoder: Geocoder = Geocoder(view.context, Locale.getDefault())
            var addresses: List<Address> = geoCoder.getFromLocation(parkingLot.coordinates!!.latitude, parkingLot.coordinates!!.longitude, 1)
            description.text = parkingLot.description
            coordinates.text = addresses.get(0).getAddressLine(0)

//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    onItemClickListener.onAlbumClick(album);
//                }
//            });
        }

        init {
            description = view.findViewById(R.id.tv_description)
            coordinates = view.findViewById(R.id.tv_coordinates)
        }
    }
}