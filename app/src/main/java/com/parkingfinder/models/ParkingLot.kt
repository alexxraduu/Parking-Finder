package com.parkingfinder.models

import android.app.Activity
import com.google.firebase.firestore.GeoPoint

class ParkingLot(var UID: String?, var locality: String?, var coordinates: GeoPoint?, var address: String?, var description: String?, var isPrivate: Boolean?) {

}