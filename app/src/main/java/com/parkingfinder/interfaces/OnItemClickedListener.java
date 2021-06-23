package com.parkingfinder.interfaces;

import com.google.firebase.firestore.GeoPoint;

public interface OnItemClickedListener {
    void openMaps(GeoPoint location);
}
