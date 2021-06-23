package com.parkingfinder.helper

import android.app.Application
import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.google.firebase.firestore.GeoPoint
import java.util.*

class LocationOperations {

    companion object {
        fun getCity(geoPoint: GeoPoint, context: Context?): String {
            val geoCoder = Geocoder(context, Locale.getDefault())
            var addresses: List<Address> =
                geoCoder.getFromLocation(geoPoint!!.latitude, geoPoint!!.longitude, 1)
            return addresses[0].locality
        }
        fun getAddress(geoPoint: GeoPoint, context: Context?): String {
            val geoCoder = Geocoder(context, Locale.getDefault())
            var addresses: List<Address> =
                geoCoder.getFromLocation(geoPoint!!.latitude, geoPoint!!.longitude, 1)
            return addresses[0].getAddressLine(0).toString()
        }
    }
}
