package com.example.albochallenge.activities

import android.Manifest
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.albochallenge.FirebaseStore
import com.example.albochallenge.LocationStore
import com.example.albochallenge.LocationService
import com.example.albochallenge.R
import com.example.albochallenge.extensions.isPermissionDenied
import com.example.albochallenge.extensions.isPermissionGranted
import kotlinx.android.synthetic.main.activity_share_location.*


class ShareLocationActivity : AppCompatActivity() {
    private val LOCATION_REQ_CODE = 1042


    private val locationStore: LocationStore by lazy {
        FirebaseStore()
    }


    private val locationUpdateListener: (Double, Double) -> Unit = { lat, lng ->
        locationStore.save(lat, lng)

        runOnUiThread {
            val message = "${lat} , ${lng}"
            coordinates_textview.text = message
        }
    }


    private val locationUpdates: LocationService by lazy {
        LocationService(this.baseContext, locationUpdateListener)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share_location)

        configureToolbar()


        setPermissionsBtnVisibility(show =  true)
        setStartBtnVisibility(show = false)
        setStopBtnVisibility(show = false)

        checkForPermissions()


        permissions_button.setOnClickListener {
            checkForPermissions()
        }


        start_button.setOnClickListener {
            setStartBtnVisibility(show = false)
            setStopBtnVisibility(show = true)

            locationUpdates.start()
        }


        stop_button.setOnClickListener {
            setStartBtnVisibility(show = true)
            setStopBtnVisibility(show = false)

            resetCoordinatesTextView()

            locationUpdates.stop()
        }

    }


    override fun onStop() {
        super.onStop()
        locationUpdates.stop()
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            LOCATION_REQ_CODE -> {

                if (isPermissionGranted(grantResults)) {

                    // permission was granted
                    setPermissionsBtnVisibility(show = false)
                    setStartBtnVisibility(show = true)
                    setStopBtnVisibility(show = false)

                } else {

                    // permission denied
                    setPermissionsBtnVisibility(show = true)
                    setStartBtnVisibility(show = false)
                    setStopBtnVisibility(show = false)

                    val message = getString(R.string.permissions_not_granted)
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

                }

            }
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


    private fun configureToolbar() {
        supportActionBar?.apply {
            title = getString(R.string.share_button_tile)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowCustomEnabled(true)
        }
    }


    private fun checkForPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (isPermissionDenied(Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Permission is not granted
                val request = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
                requestPermissions(request, LOCATION_REQ_CODE)

            } else {

                // Permissions granted
                setPermissionsBtnVisibility(show = false)
                setStartBtnVisibility(show = true)
                setStopBtnVisibility(show = false)

            }
        }
    }


    private fun setPermissionsBtnVisibility(show: Boolean) {
        permissions_button.visibility = if(show) View.VISIBLE else View.GONE
    }


    private fun setStartBtnVisibility(show: Boolean) {
        start_button.visibility = if(show) View.VISIBLE else View.GONE
    }


    private fun setStopBtnVisibility(show: Boolean) {
        stop_button.visibility = if(show) View.VISIBLE else View.GONE
    }

    private fun resetCoordinatesTextView() {
        coordinates_textview.text = getString(R.string.no_updates_yet)
    }
}
