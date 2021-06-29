package com.parkingfinder.interfaces

import com.google.firebase.firestore.GeoPoint
import com.parkingfinder.models.ParkingLot

interface OnItemClickedListener {
    fun openMaps(location: GeoPoint?)
    fun openParkingLotView(parkingLot: ParkingLot?)
}