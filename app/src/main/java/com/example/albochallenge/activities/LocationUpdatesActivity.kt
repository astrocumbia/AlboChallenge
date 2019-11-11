package com.example.albochallenge.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.albochallenge.FirebaseUtils
import com.example.albochallenge.services.FirebaseStoreService
import com.example.albochallenge.services.LocationStore
import com.example.albochallenge.R
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_share_location.coordinates_textview
import kotlinx.android.synthetic.main.activity_share_location.start_button
import kotlinx.android.synthetic.main.activity_share_location.stop_button



class LocationUpdatesActivity : AppCompatActivity() {

    private val locationStore: LocationStore by lazy {
        FirebaseStoreService()
    }

    private val locationUpdatesListener: (Double, Double) -> Unit = { lat, lng ->
        runOnUiThread {
            val message = "$lat , $lng"
            coordinates_textview.text = message
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_updates)

        configureToolbar()

        setStopVisibility(show = false)


        start_button.setOnClickListener {
            setStartVisibility(show = false)
            setStopVisibility(show = true)

            locationStore.getLocationUpdates(locationUpdatesListener)
            FirebaseUtils.subscribeToNotifications()
        }


        stop_button.setOnClickListener {

            setStartVisibility(show = true)
            setStopVisibility(show = false)

            locationStore.stopLocationUpdates()

            resetCoordinatesTextView()
            FirebaseUtils.unsubscribeToNofifications()
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun configureToolbar() {
        supportActionBar?.apply {
            title = getString(R.string.receive_button_tile)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowCustomEnabled(true)
        }
    }


    private fun setStartVisibility(show: Boolean) {
        start_button.visibility = if (show) View.VISIBLE else View.GONE
    }


    private fun setStopVisibility(show: Boolean) {
        stop_button.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun resetCoordinatesTextView() {
        coordinates_textview.text = getString(R.string.no_updates_yet)
    }
}
