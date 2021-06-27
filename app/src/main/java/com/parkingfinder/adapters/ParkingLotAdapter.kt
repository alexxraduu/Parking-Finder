package com.parkingfinder.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.VolleyError
import com.android.volley.toolbox.ImageLoader.ImageContainer
import com.android.volley.toolbox.ImageLoader.ImageListener
import com.parkingfinder.R
import com.parkingfinder.helper.LocationOperations.Companion.getAddress
import com.parkingfinder.interfaces.OnItemClickedListener
import com.parkingfinder.models.ParkingLot
import com.parkingfinder.singletons.VolleyConfigSingleton
import java.util.*


class ParkingLotAdapter(private val parkingLots: ArrayList<ParkingLot>, val onItemClickedListener: OnItemClickedListener) :
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
        private val address: TextView
        private val imageView: ImageView
        fun bind(parkingLot: ParkingLot) {

            description.text = parkingLot.description
            address.text=getAddress(parkingLot.coordinates!!,view.context)
            view.setOnClickListener { onItemClickedListener.openParkingLotView(parkingLot) }

            val imageViewUrl =
                "https://logodownload.org/wp-content/uploads/2018/01/google-maps-logo-0.png"
            val imageLoader = VolleyConfigSingleton.getInstance(
                imageView.context.applicationContext
            ).imageLoader
            imageLoader[imageViewUrl, object : ImageListener {
                override fun onResponse(response: ImageContainer, isImmediate: Boolean) {
                    imageView.setImageBitmap(response.bitmap)
                }

                override fun onErrorResponse(error: VolleyError) {}
            }]

            imageView.setOnClickListener { onItemClickedListener.openMaps(parkingLot.coordinates) }


        }

        init {
            description = view.findViewById(R.id.tv_description)
            address = view.findViewById(R.id.tv_address)
            imageView = view.findViewById(R.id.imageView)
        }
    }


}