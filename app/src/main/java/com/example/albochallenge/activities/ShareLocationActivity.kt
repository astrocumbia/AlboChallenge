package com.example.albochallenge.activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.example.albochallenge.FirebaseStore
import com.example.albochallenge.LocationStore
import com.example.albochallenge.LocationService
import com.example.albochallenge.R
import com.example.albochallenge.network.FCMService
import com.example.albochallenge.network.Notification
import com.example.albochallenge.network.NotificationBody
import kotlinx.android.synthetic.main.activity_share_location.*


class ShareLocationActivity : AppCompatActivity() {
    private val TAG = ShareLocationActivity::class.java.canonicalName.toString()

    private val LOCATION_REQ_CODE = 1042


    private val locationUpdates: LocationService by lazy {
        LocationService(this.baseContext) { lat, lng ->
            locationStore.save(lat, lng)
            sendNotification()

            val message = "${lat} , ${lng}"

            coordinates_textview.text = message
        }
    }

    private val locationStore: LocationStore by lazy {
        FirebaseStore()
    }

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

            locationUpdates.start()
        }


        stop_button.setOnClickListener {
            stop_button.visibility = View.GONE
            location_title_textview.visibility = View.GONE
            coordinates_textview.visibility = View.GONE

            start_button.visibility = View.VISIBLE

            locationUpdates.stop()
        }

    }

    private fun sendNotification() {
        val service = FCMService()
        val notificationBody = NotificationBody("title", "message")
        val notification = Notification("/topics/topic01", notificationBody)

        service.send(notification,
            success = {
                Log.e(TAG, "onSuccess")
            },
            failure = {
                Log.e(TAG, "onFailure ${it}")
            })
    }


    override fun onStop() {
        super.onStop()
        locationUpdates.stop()
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
