package com.example.albochallenge.activities

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import com.example.albochallenge.R


class MainActivity : AppCompatActivity() {

//    private val mediaPlayer: MediaPlayer by lazy {
//        MediaPlayer.create(applicationContext,
//            R.raw.sample).apply {
//            setVolume(0.06f, 0.06f)
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
