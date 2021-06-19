package com.parkingfinder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.parkingfinder.fragments.LoginRegister

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addLoginRegisterFragment()
    }

    fun addLoginRegisterFragment() {
        val fragmentManager: FragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = LoginRegister::class.java.getName()
        val addTransaction: FragmentTransaction = transaction.add(
            R.id.frame_layout, LoginRegister(), tag
        )
        addTransaction.commit()
    }
}