package com.parkingfinder.models

import com.google.firebase.firestore.GeoPoint

class ParkingLot(val description: String?, val coordinates: GeoPoint?) {
}