package com.example.albochallenge.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.albochallenge.FirebaseStore
import com.example.albochallenge.LocationStore
import com.example.albochallenge.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_location_updates.*
import kotlinx.android.synthetic.main.activity_share_location.coordinates_textview
import kotlinx.android.synthetic.main.activity_share_location.start_button
import kotlinx.android.synthetic.main.activity_share_location.stop_button

class LocationUpdatesActivity : AppCompatActivity() {
    private val TAG = LocationUpdatesActivity::class.java.canonicalName.toString()

    private val locationStore: LocationStore by lazy {
        FirebaseStore()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_updates)

        start_button.setOnClickListener {
            start_button.visibility = View.GONE
            stop_button.visibility = View.VISIBLE

            locationStore.getLocationUpdates { lat, lng ->
                runOnUiThread {
                    val message = "$lat , $lng"
                    coordinates_textview.text = message
                }
            }

            FirebaseMessaging.getInstance().subscribeToTopic("topic01")
        }

        stop_button.setOnClickListener {
            start_button.visibility = View.VISIBLE
            stop_button.visibility = View.GONE

            locationStore.stopLocationUpdates()


            FirebaseMessaging.getInstance().unsubscribeFromTopic("topic01")
        }

    }


}
