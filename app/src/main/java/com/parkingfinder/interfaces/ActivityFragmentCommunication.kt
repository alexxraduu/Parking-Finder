package com.parkingfinder.interfaces

import com.parkingfinder.models.ParkingLot

interface ActivityFragmentCommunication {
    fun addParkingLotViewFragment(parkingLot: ParkingLot?)
    fun addDevelopersInfoFragment(info: String?)
}