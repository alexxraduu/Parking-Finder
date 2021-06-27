package com.parkingfinder.models
import com.google.firebase.firestore.GeoPoint

class ParkingLot(var UID: String?, var locality: String?, var coordinates: GeoPoint?, var address: String?, var description: String?, var isPrivate: Boolean?) {

}