package com.parkingfinder.interfaces;

import com.parkingfinder.models.ParkingLot;

public interface ActivityFragmentCommunication {
    void addParkingLotViewFragment(ParkingLot parkingLot);
    void addDevelopersInfoFragment(String info);
}
