package com.example.albochallenge

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ListenerRegistration


interface LocationStore {

    fun save(latitude: Double, longitude: Double)

    fun getLocationUpdates(l: (Double, Double) -> Unit)

    fun stopLocationUpdates()
}


class FirebaseStore: LocationStore {

    private var listenerRegistration: ListenerRegistration? = null

    private val firebaseRef by lazy {
        val db = FirebaseFirestore.getInstance()

        db.collection("location").document("topic01")
    }


    override fun save(latitude: Double, longitude: Double) {
        val coordinates = GeoPoint(latitude, longitude)
        val map = mutableMapOf<String, Any>()

        map["coordinates"] = coordinates

        firebaseRef.set(map)
    }


    override fun getLocationUpdates(l: (Double, Double) -> Unit) {
        listenerRegistration = firebaseRef.addSnapshotListener { snapshot, error ->

            if (snapshot != null && snapshot.exists()) {
                val coordinates = snapshot.getGeoPoint("coordinates")
                coordinates?.let { c ->
                    l(c.latitude, c.longitude)
                }
            }

        }
    }

    override fun stopLocationUpdates() {
        listenerRegistration?.remove()
        listenerRegistration = null
    }

}