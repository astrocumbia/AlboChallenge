package com.example.albochallenge.extensions

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

fun AppCompatActivity.isPermissionGranted(grantResults: IntArray) : Boolean {
    return grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
}

fun AppCompatActivity.isPermissionDenied(permission: String) : Boolean {
    val permissionStatus = ContextCompat.checkSelfPermission(this, permission)
    return permissionStatus !== PackageManager.PERMISSION_GRANTED
}
