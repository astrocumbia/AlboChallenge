package com.example.albochallenge.services

import android.util.Log
import com.example.albochallenge.FirebaseUtils.Companion.TOPIC_PATH
import com.example.albochallenge.network.FCMService
import com.example.albochallenge.network.Notification
import com.example.albochallenge.network.NotificationBody

class NotificationService(key: String) {
    private val TAG = NotificationService::class.java.canonicalName.toString()


    private val fcmService by lazy {
        FCMService(key)
    }


    private val notificationBody by lazy {
        NotificationBody("Ubicación", "Nueva ubicación obtenida")
    }


    private val notification by lazy {
        Notification(TOPIC_PATH, notificationBody)
    }


    fun send() {
        fcmService.send(notification,
            success = {
                Log.i(TAG, "onSuccess")
            },
            failure = {
                Log.e(TAG, "onFailure ${it}")
            })
    }
}