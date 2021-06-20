package com.parkingfinder.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.parkingfinder.R
import com.parkingfinder.fragments.ParkingList
import com.parkingfinder.interfaces.ActivityFragmentCommunication


class SecondActivity : AppCompatActivity(), ActivityFragmentCommunication {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        addParkingListFragment()
    }

    fun addParkingListFragment() {
        val fragmentManager: FragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = ParkingList::class.java.getName()
        val addTransaction: FragmentTransaction = transaction.add(
            R.id.frame_layout_second, ParkingList(), tag
        )
        addTransaction.commit()
    }
}