package com.parkingfinder.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.parkingfinder.R
import com.parkingfinder.fragments.AddEditParkingLot
import com.parkingfinder.interfaces.ActivityFragmentCommunication
import com.parkingfinder.models.ParkingLot


class AddEditActivity : AppCompatActivity(), ActivityFragmentCommunication {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)
        addAddParkingLotFragment()
    }

    private fun addAddParkingLotFragment() {
        val fragmentManager: FragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = AddEditParkingLot::class.java.name
        val addTransaction: FragmentTransaction = transaction.add(
            R.id.frame_layout_third, AddEditParkingLot(), tag
        )
        addTransaction.commit()
    }

    override fun addParkingLotViewFragment(parkingLot: ParkingLot?) {
        TODO("Not yet implemented")
    }

    override fun addDevelopersInfoFragment(info: String?) {
        TODO("Not yet implemented")
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        CheckSettingsActivity.checkPhoneSettings(this)
        CheckSettingsActivity.checkPermissions(this)
    }
}