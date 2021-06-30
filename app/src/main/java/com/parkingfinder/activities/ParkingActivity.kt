package com.parkingfinder.activities

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.parkingfinder.R
import com.parkingfinder.fragments.DevelopersInfo
import com.parkingfinder.fragments.ParkingList
import com.parkingfinder.fragments.ParkingLotView
import com.parkingfinder.interfaces.ActivityFragmentCommunication
import com.parkingfinder.models.ParkingLot
import com.parkingfinder.services.LocationBackgroundService

class ParkingActivity : AppCompatActivity(), ActivityFragmentCommunication {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        addParkingListFragment()

        if (isMyServiceRunning(LocationBackgroundService::class.java)) {
            Log.d("Location", "Service already running...")
        } else {
            startService(Intent(this, LocationBackgroundService::class.java))
        }
    }

    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

    private fun addParkingListFragment() {
        val fragmentManager: FragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = ParkingList::class.java.name
        val addTransaction: FragmentTransaction = transaction.add(
            R.id.frame_layout_second, ParkingList(), tag
        )
        addTransaction.commit()
    }

    override fun addParkingLotViewFragment(parkingLot: ParkingLot?) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = ParkingLotView::class.java.name
        val replaceTransaction: FragmentTransaction = transaction.replace(
            R.id.frame_layout_second, ParkingLotView(parkingLot!!), tag
        )
        replaceTransaction.addToBackStack(tag)
        replaceTransaction.commit()
    }

    override fun addDevelopersInfoFragment(info: String?) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = DevelopersInfo::class.java.name
        val replaceTransaction: FragmentTransaction = transaction.replace(
            R.id.frame_layout_second, DevelopersInfo(info!!), tag
        )
        replaceTransaction.addToBackStack(tag)
        replaceTransaction.commit()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        CheckSettingsActivity.checkPhoneSettings(this)
        CheckSettingsActivity.checkPermissions(this)
    }
}