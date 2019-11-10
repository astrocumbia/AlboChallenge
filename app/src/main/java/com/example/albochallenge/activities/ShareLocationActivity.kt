package com.example.albochallenge.activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.example.albochallenge.R
import com.google.android.gms.location.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import kotlinx.android.synthetic.main.activity_share_location.*



class ShareLocationActivity : AppCompatActivity() {
    private val TAG = ShareLocationActivity::class.java.canonicalName.toString()

    private val LOCATION_REQ_CODE = 1042

    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }


    private val locationRequest: LocationRequest by lazy {
        LocationRequest.create()?.apply {
            interval = 1000
            fastestInterval = 1000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }!!
    }

    private val firebaseRef by lazy {
        val db = FirebaseFirestore.getInstance()

        db.collection("location").document("topic01")
    }

    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult ?: return
            for (location in locationResult.locations){

                val coordinates = GeoPoint(location.latitude, location.longitude)

                val map = mutableMapOf<String, Any>()


                map["coordinates"] = coordinates

                firebaseRef.set(map)

                runOnUiThread {
                    val message = "${location.latitude} , ${location.longitude}"
                    coordinates_textview.text = message
                }
            }
        }
    }


    /*
    *
    *
    * LocationService(this)
    *
    * LocatinService.init(this)
    *
    * LocationService.start()
    *
    * LocationService.stop()
    *
    * LocationService.destroy()
    *
    * */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share_location)


        // onClickStart
        // 1.- Check for permissions
        // 2.- Ask permision
        // 3.- If dont have show snackbar with permissions
        // 4.- if have, start service
        // 5.- Update button to Stop
        // 6.- Show location


        stop_button.visibility = View.GONE
        location_title_textview.visibility = View.GONE
        coordinates_textview.visibility = View.GONE

        checkForPermissions()


        start_button.setOnClickListener {
            stop_button.visibility = View.VISIBLE
            location_title_textview.visibility = View.VISIBLE
            coordinates_textview.visibility = View.VISIBLE

            start_button.visibility = View.GONE

            startLocationUpdates()
        }


        stop_button.setOnClickListener {
            stop_button.visibility = View.GONE
            location_title_textview.visibility = View.GONE
            coordinates_textview.visibility = View.GONE

            start_button.visibility = View.VISIBLE

            stopLocationUpdates()
        }

    }




    private fun startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(locationRequest,
            locationCallback,
            Looper.getMainLooper())
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
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
                    //show toast

                    Log.e(TAG, "onRequestPermissionResult denied")
                }
                return
            }
        }
    }
}
