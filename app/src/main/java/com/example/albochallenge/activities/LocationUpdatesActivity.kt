package com.example.albochallenge.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.albochallenge.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.android.synthetic.main.activity_location_updates.*
import kotlinx.android.synthetic.main.activity_share_location.coordinates_textview
import kotlinx.android.synthetic.main.activity_share_location.start_button
import kotlinx.android.synthetic.main.activity_share_location.stop_button

class LocationUpdatesActivity : AppCompatActivity() {
    private val TAG = LocationUpdatesActivity::class.java.canonicalName.toString()

    val db = FirebaseFirestore.getInstance()
    val ref = db.collection("location").document("topic01")

    lateinit var  listenerRegistration: ListenerRegistration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_updates)

        start_button.setOnClickListener {
            start_button.visibility = View.GONE
            stop_button.visibility = View.VISIBLE

            startReceiveLocation()
        }

        stop_button.setOnClickListener {
            start_button.visibility = View.VISIBLE
            stop_button.visibility = View.GONE

            stopReceiveLocation()
        }






    }

    private fun startReceiveLocation() {
        listenerRegistration = ref.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.w(TAG, "Listen failed.", error)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val coordinates = snapshot.getGeoPoint("coordinates")
                coordinates?.let {
                    runOnUiThread {
                        val message = "${it.latitude} , ${it.longitude}"
                        coordinates_textview.text = message
                    }
                }
                Log.d(TAG, "Current data: ${coordinates}")
            }

        }

    }

    private fun stopReceiveLocation() {
        listenerRegistration.remove()
    }
}
