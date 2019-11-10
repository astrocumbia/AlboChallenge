package com.example.albochallenge

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*


class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.java.canonicalName.toString()
    private val LOCATION_REQ_CODE = 1042


    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkForPermissions()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        startLocationUpdates()
    }

    private fun startLocationUpdates() {

        val locationRequest = LocationRequest.create()?.apply {
            interval = 1000
            fastestInterval = 1000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }!!


        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations){
                    runOnUiThread {
                        Log.e(TAG, "New location received, ${location.latitude} , ${location.longitude}")
                    }
                    // Update UI with location data
                    // ...
                }
            }
        }


        fusedLocationClient.requestLocationUpdates(locationRequest,
            locationCallback,
            Looper.getMainLooper())
    }



    // TODO: refactor this
    private fun checkForPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
                // check for shouldShowRequestPermissionRational

                // Permission is not granted
                Log.e(TAG, "permission not granted")

                val request = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
                requestPermissions(request, LOCATION_REQ_CODE)
            } else {
                Log.e(TAG, "permission granted")

            }
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            LOCATION_REQ_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, do your work....
                    Log.e(TAG, "onRequestPermissionResult granted")
                } else {
                    // permission denied
                    // Disable the functionality that depends on this permission.

                    Log.e(TAG, "onRequestPermissionResult denied")
                }
                return
            }
        }
    }
}
