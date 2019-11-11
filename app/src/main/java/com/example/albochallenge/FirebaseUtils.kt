package com.example.albochallenge

import com.google.firebase.messaging.FirebaseMessaging

class FirebaseUtils {
    companion object {
        const val TOPIC = "topic01"
        const val TOPIC_PATH = "/topics/${TOPIC}"

        fun subscribeToNotifications() {
            FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)
        }

        fun unsubscribeToNofifications() {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(TOPIC)
        }

    }
}