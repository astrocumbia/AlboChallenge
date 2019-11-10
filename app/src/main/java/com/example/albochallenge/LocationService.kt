package com.example.albochallenge

import android.content.Context
import android.os.Looper
import com.google.android.gms.location.*

class LocationService(context: Context, onLocationUpdate: (Double, Double) -> Unit) {

    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }


    private val locationRequest: LocationRequest by lazy {
        LocationRequest.create()?.apply {
            interval = 1000
            fastestInterval = 1000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }!!
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult ?: return
            for (location in locationResult.locations){
                onLocationUpdate(location.latitude, location.longitude)
            }
        }
    }


    fun start() {
        fusedLocationClient.requestLocationUpdates(locationRequest,
            locationCallback,
            Looper.getMainLooper())
    }

    fun stop() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
}