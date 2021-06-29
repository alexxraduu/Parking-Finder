package com.parkingfinder.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.parkingfinder.R

class CheckSettingsActivity : AppCompatActivity() {
    private var btnExit: Button? = null
    private var btnContinue: Button? = null

    companion object {
        fun checkLocationServices(context: Context): Boolean {
            var lm: LocationManager =
                (context.getSystemService(Context.LOCATION_SERVICE) as LocationManager?)!!
            return lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        }

        @SuppressLint("MissingPermission")
        fun checkNetworkServices(context: Context): Boolean {
            var cm: ConnectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
            return activeNetwork?.isConnectedOrConnecting == true
        }

        private fun openCheckSettingsActivity(context: Context) {
            val intent = Intent(context, CheckSettingsActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent)
        }

        fun checkPhoneSettings(context: Context) {
            if (!(checkLocationServices(context) && checkNetworkServices(context))) {
                openCheckSettingsActivity(context)
            }
        }

        fun checkPermissions(activity: Activity) {
            if (ContextCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                activity.requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    1001
                )
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_settings)

        btnExit = findViewById(R.id.btn_exit)
        btnContinue = findViewById(R.id.btn_continue)

        btnExit?.setOnClickListener() {
            exitApp()
        }

        btnContinue?.setOnClickListener() {
            if (checkLocationServices(this) && checkNetworkServices(this)) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Try again!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun exitApp() {
        finishAffinity();
    }
}