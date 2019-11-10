package com.example.albochallenge.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.albochallenge.R
import com.google.firebase.firestore.FirebaseFirestore

class LocationUpdatesActivity : AppCompatActivity() {
    private val TAG = LocationUpdatesActivity::class.java.canonicalName.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_updates)

        val db = FirebaseFirestore.getInstance()
        val ref = db.collection("location").document("topic01")

        ref.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.w(TAG, "Listen failed.", error)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                Log.d(TAG, "Current data: ${snapshot.data}")
            } else {
                Log.d(TAG, "Current data: null")
            }

        }
    }
}
