package com.parkingfinder.interfaces;

import com.google.firebase.firestore.GeoPoint;
import com.parkingfinder.models.ParkingLot;

public interface OnItemClickedListener {
    void openMaps(GeoPoint location);
    void openParkingLotView(ParkingLot parkingLot);
}
