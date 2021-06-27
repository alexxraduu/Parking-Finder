package com.parkingfinder.models

import android.app.Activity
import com.google.firebase.firestore.GeoPoint

class ParkingLot(val UID: String?, val locality: String?, val coordinates: GeoPoint?, val address: String?,val description: String?, val isPrivate: Boolean?) {

}