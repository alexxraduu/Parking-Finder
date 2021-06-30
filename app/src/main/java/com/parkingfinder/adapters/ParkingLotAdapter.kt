package com.parkingfinder.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.parkingfinder.R
import com.parkingfinder.helper.LocationOperations
import com.parkingfinder.helper.LocationOperations.Companion.getAddress
import com.parkingfinder.interfaces.OnItemClickedListener
import com.parkingfinder.models.ParkingLot
import java.util.*

class ParkingLotAdapter(
    private val parkingLots: ArrayList<ParkingLot>,
    val onItemClickedListener: OnItemClickedListener
) :
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

    fun distanceString(value: Int): String {
        return when (value) {
            in 0..999 -> "$value meters away"
            else -> "${value / 1000} km away"
        }
    }

    internal inner class ParkingLotViewHolder(private val view: View) : RecyclerView.ViewHolder(
        view
    ) {

        private val description: TextView
        private val address: TextView
        private val imageView: ImageView
        private val distance: TextView
        fun bind(parkingLot: ParkingLot) {
            description.text = parkingLot.description
            address.text = getAddress(parkingLot.coordinates!!, view.context)
            distance.text = distanceString(
                LocationOperations.distanceBetweenGeoPoints(
                    parkingLot.coordinates!!,
                    LocationOperations.myLocation
                )
            )
            view.setOnClickListener { onItemClickedListener.openParkingLotView(parkingLot) }
            imageView.setOnClickListener { onItemClickedListener.openMaps(parkingLot.coordinates) }
        }

        init {
            description = view.findViewById(R.id.tv_description)
            address = view.findViewById(R.id.tv_address)
            imageView = view.findViewById(R.id.imageView)
            distance = view.findViewById(R.id.tv_distance)
        }
    }


}