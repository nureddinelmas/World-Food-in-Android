package com.se.iths.app21.grupp1.myapplication.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.se.iths.app21.grupp1.myapplication.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Handler().postDelayed({
            val intent = Intent(this@MainActivity, MapsActivity::class.java)
            startActivity(intent)
        }, 2000)

    }


}