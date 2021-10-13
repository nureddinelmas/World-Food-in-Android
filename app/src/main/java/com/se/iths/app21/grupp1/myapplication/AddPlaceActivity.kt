package com.se.iths.app21.grupp1.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.se.iths.app21.grupp1.myapplication.databinding.ActivityAddPlaceBinding

class AddPlaceActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddPlaceBinding

    lateinit var auth: FirebaseAuth
    lateinit var db : FirebaseFirestore

    var lat: Double? = null
    var long: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPlaceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val intent = intent
        lat = intent.getDoubleExtra("lat",0.0)
        long = intent.getDoubleExtra("long", 0.0)

        println(lat)
        println(long)

        binding.saveButton.setOnClickListener {
            savePlaces()
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }

    }

    private fun savePlaces(){

        val places = hashMapOf<String, Any>()

        if(auth.currentUser != null){
            places["userEmail"] = auth.currentUser!!.email!!
            places["name"] = binding.placeNameText.text.toString()
            places["land"] = binding.landText.text.toString()
            places["lat"] = lat!!.toDouble()
            places["long"] = long!!.toDouble()
            places["beskrivning"] = binding.beskrivningText.text.toString()
            places["date"] = Timestamp.now()

            db.collection("Places" ).add(places).addOnSuccessListener {
                finish()
            }.addOnFailureListener {
                Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }
    }


    fun givePermissin(){

    }

}