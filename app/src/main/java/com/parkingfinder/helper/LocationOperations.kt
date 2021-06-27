package com.parkingfinder.helper

import android.app.Application
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.firestore.GeoPoint
import java.util.*

class LocationOperations {


    companion object {
        fun openGoogleMaps(location: GeoPoint, context: Context?) {
            val gmmIntentUri = Uri.parse("geo:0,0?q=${location!!.latitude},${location!!.longitude}")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            context?.startActivity(mapIntent)
        }

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
