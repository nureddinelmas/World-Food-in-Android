package com.se.iths.app21.grupp1.myapplication

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Adapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newrecyclerview.DescriptionRecyclerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.se.iths.app21.grupp1.myapplication.databinding.ActivityPlacesBinding
import kotlinx.android.synthetic.main.activity_places.*


class PlacesActivity : AppCompatActivity() {

    lateinit var binding: ActivityPlacesBinding

    var commentslist : MutableList<Comments> = mutableListOf()
    var placeslist : MutableList<Place> = mutableListOf()
    lateinit var auth: FirebaseAuth
    lateinit var db : FirebaseFirestore
    lateinit var storage : FirebaseStorage
    var mAdapter: DescriptionRecyclerAdapter? = null
    lateinit var recyclerDescription: RecyclerView
    lateinit var collectionRef: CollectionReference
    var TAG: String = "!!!"
    var lat : Double? = null
    var long : Double? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlacesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        mAdapter = DescriptionRecyclerAdapter(this, commentslist)
        recyclerDescription.layoutManager = LinearLayoutManager(this)
        recyclerDescription.setHasFixedSize(true)
        recyclerDescription.adapter = mAdapter
        var comments = mutableListOf<Comments>()

        val intent = intent
        val lat = intent.getDoubleExtra("lat", 0.0)
        val long = intent.getDoubleExtra("long",0.0)

        // Måste också skicka med placesId från MapsActivity
        val commentslist = intent.getStringExtra("placeId")


        getPlacesInfo()
      //  getComments()

        binding.buttonSaveDescription.setOnClickListener {
            saveDescription()
        }

    }

    fun getPlacesInfo() {

        db.collection("Places").whereEqualTo("lat", lat ).whereEqualTo("long", long)
            .get()
            .addOnSuccessListener {

                if (it.isEmpty) {
                    Toast.makeText(this, "No place found", Toast.LENGTH_LONG).show()
                    return@addOnSuccessListener
                }

                    for (document in it) {
                       val placeModel = document.toObject(Place::class.java)
                        placeslist.add(placeModel)
                        Log.d("!!!", "Document: $document")
                    }
                }
            }

  /*  fun getComments() {

        db.collection("Comments").whereEqualTo("placeId", placeId)
            .get()
            .addOnSuccessListener {
                if(it.isEmpty) {
                    Toast.makeText(this, "No comments found", Toast.LENGTH_LONG).show()
                    return@addOnSuccessListener
            }
                for(document in it) {
                    val commentsModel = document.toObject(Comments::class.java)
                    commentslist.add(commentsModel)
                }
                binding.recyclerDescription.apply{
                   mAdapter
               }
    }

    }

   */



    fun saveDescription() {
        val places = hashMapOf<String, Any>()

        if(auth.currentUser != null) {
            places["beskrivning"] = binding.addDescriptionText.text.toString()

            db.collection("Places" ).add(places).addOnSuccessListener {
                finish()
            }.addOnFailureListener {
                Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }
    }


    }
