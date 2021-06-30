package com.parkingfinder.services

import android.annotation.SuppressLint
import android.app.*
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.parkingfinder.R
import com.parkingfinder.activities.LoginRegisterActivity
import com.parkingfinder.helper.LocationOperations
import java.util.*
import kotlin.concurrent.thread

class LocationBackgroundService : Service() {
    private val CHANNEL_ID = "ParkingFinder"
    private var locationCallback: LocationCallback? = null
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var myCoordinates: GeoPoint = GeoPoint(21.305611, -157.858566) // hawaii

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationClient?.removeLocationUpdates(locationCallback)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()
        val notificationIntent = Intent(this, LoginRegisterActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, 0
        )
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("ParkingFinder")
            .setContentText("Loading...")
            .setSmallIcon(R.drawable.z_logo_launcher)
            .setContentIntent(pendingIntent)
            .build()
        startForeground(1, notification)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    Log.d("Location", "[${location.latitude}, ${location.longitude}]")
                    myCoordinates = GeoPoint(location.latitude, location.longitude)
                }
            }
        }
        startLocationUpdates()
        repeat(pendingIntent)
        return START_NOT_STICKY
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        val locationRequest: LocationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 10000
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient?.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private fun repeat(intent: PendingIntent) {
        thread {
            backgroundTask(intent)
            Thread.sleep(2000)
            repeat(intent)
        }
    }

    private fun backgroundTask(intent: PendingIntent) {
        val db = FirebaseFirestore.getInstance()
        db.collection("parking-lot")
            .whereEqualTo(
                "locality",
                LocationOperations.getLocality(myCoordinates, this).toLowerCase(Locale.ROOT)
            )
            .get()
            .addOnSuccessListener { documents ->
                val manage = getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager
                val notification1 = NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("ParkingFinder")
                    .setContentText(
                        "There are ${documents.size()} parking lots in ${
                            LocationOperations.getLocality(
                                myCoordinates,
                                this
                            )
                        } right now!"
                    )
                    .setSmallIcon(R.drawable.z_logo_launcher)
                    .setContentIntent(intent)
                    .build()
                manage.notify(1, notification1)
            }
            .addOnFailureListener { exception ->
                Log.w("FIREBASE", "Error getting documents:", exception)
            }
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID, "ParkingFinder",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager!!.createNotificationChannel(serviceChannel)
        }
    }
}