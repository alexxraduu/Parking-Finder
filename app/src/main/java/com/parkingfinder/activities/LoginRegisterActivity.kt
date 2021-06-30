package com.parkingfinder.activities

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.parkingfinder.R
import com.parkingfinder.fragments.LoginRegister


class LoginRegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        CheckSettingsActivity.checkPhoneSettings(this)
        CheckSettingsActivity.checkPermissions(this)
        addLoginRegisterFragment()
    }

    // when you don't give access to location
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty())
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                finish()
            }
    }

    private fun addLoginRegisterFragment() {
        val fragmentManager: FragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = LoginRegister::class.java.name
        val addTransaction: FragmentTransaction = transaction.add(
            R.id.frame_layout, LoginRegister(), tag
        )
        addTransaction.commit()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        CheckSettingsActivity.checkPhoneSettings(this)
        CheckSettingsActivity.checkPermissions(this)
    }
}