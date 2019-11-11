package com.example.albochallenge.activities

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import com.example.albochallenge.R


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)

        share_location_btn.setOnClickListener {
            val intent = Intent(this, ShareLocationActivity::class.java)
            startActivity(intent)
        }

        receive_location_btn.setOnClickListener {
            val intent = Intent(this, LocationUpdatesActivity::class.java)
            startActivity(intent)
        }
    }

}
