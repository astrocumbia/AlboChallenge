package com.example.albochallenge.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.albochallenge.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

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
