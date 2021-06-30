package com.parkingfinder.helper

import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import com.google.firebase.firestore.GeoPoint
import java.util.*

class LocationOperations {


    companion object {
        var myLocation: GeoPoint = GeoPoint(0.0, 0.0)
        var searchedLocality: String = ""

        fun openGoogleMaps(location: GeoPoint, context: Context?) {
            val gmmIntentUri = Uri.parse("geo:0,0?q=${location.latitude},${location.longitude}")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            context?.startActivity(mapIntent)
        }

        fun getLocality(geoPoint: GeoPoint, context: Context?): String {
            val geoCoder = Geocoder(context, Locale.getDefault())
            val addresses: List<Address> =
                geoCoder.getFromLocation(geoPoint.latitude, geoPoint.longitude, 1)
            return addresses[0].locality
        }

        fun getAddress(geoPoint: GeoPoint, context: Context?): String {
            val geoCoder = Geocoder(context, Locale.getDefault())
            val addresses: List<Address> =
                geoCoder.getFromLocation(geoPoint.latitude, geoPoint.longitude, 1)
            return addresses[0].getAddressLine(0).toString()
        }

        fun distanceBetweenGeoPoints(source: GeoPoint, destination: GeoPoint): Int {
            val startPoint = Location("")
            startPoint.latitude = source.latitude
            startPoint.longitude = source.longitude
            val endPoint = Location("")
            endPoint.latitude = destination.latitude
            endPoint.longitude = destination.longitude
            return startPoint.distanceTo(endPoint).toInt()
        }
    }
}
