package com.parkingfinder.activities

import android.location.LocationListener
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.gms.location.LocationRequest
import com.parkingfinder.R
import com.parkingfinder.fragments.DevelopersInfo
import com.parkingfinder.fragments.ParkingList
import com.parkingfinder.fragments.ParkingLotView
import com.parkingfinder.interfaces.ActivityFragmentCommunication
import com.parkingfinder.models.ParkingLot


class SecondActivity : AppCompatActivity(), ActivityFragmentCommunication {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        addParkingListFragment()
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