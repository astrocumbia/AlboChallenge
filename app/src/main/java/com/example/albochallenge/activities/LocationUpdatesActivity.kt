package com.example.albochallenge.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.albochallenge.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_location_updates.*
import com.google.firebase.firestore.FirebaseFirestore


class LocationUpdatesActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    val TAG = LocationUpdatesActivity::class.java.canonicalName.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_updates)

//        auth = FirebaseAuth.getInstance()
//
//        auth.signInAnonymously()
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    // Sign in success, update UI with the signed-in user's information
//                    Log.d(TAG, "signInAnonymously:success")
//                    val user = auth.currentUser
//                    readDB()
//                } else {
//                    // If sign in fails, display a message to the user.
//                    Log.w(TAG, "signInAnonymously:failure", task.exception)
//                    Toast.makeText(baseContext, "Authentication failed.",
//                        Toast.LENGTH_SHORT).show()
//                }
//            }




        start_update_location.setOnClickListener {
            FirebaseMessaging.getInstance().subscribeToTopic("topic01")
            readDB()
            Toast.makeText(this, "Suscrito a topic01", Toast.LENGTH_LONG).show()
        }
    }

    private fun readDB() {

        val db = FirebaseFirestore.getInstance()
        val ref = db.collection("location").document("topic01")

        ref.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
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
