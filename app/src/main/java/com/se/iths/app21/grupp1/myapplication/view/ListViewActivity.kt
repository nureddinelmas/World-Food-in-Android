package com.se.iths.app21.grupp1.myapplication.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.se.iths.app21.grupp1.myapplication.adapter.ListViewAdapter
import com.se.iths.app21.grupp1.myapplication.databinding.ActivityListViewBinding
import com.se.iths.app21.grupp1.myapplication.model.Places
import kotlinx.android.synthetic.main.activity_list_view.*

class ListViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListViewBinding
    lateinit var listViewAdapter : ListViewAdapter
    lateinit var placeList : ArrayList<Places>
    lateinit var db : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

       db  = FirebaseFirestore.getInstance()
        binding.listViewRecyclerView.layoutManager = LinearLayoutManager(this)

        placeList = ArrayList<Places>()

        mapsImageView.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }
        getData()

        listViewAdapter = ListViewAdapter(placeList)
        listViewRecyclerView.adapter = listViewAdapter
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                listViewAdapter.filter.filter(query)
                listViewAdapter.notifyDataSetChanged()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                listViewAdapter.filter.filter(newText)
                listViewAdapter.notifyDataSetChanged()
                return false
            }

        })




    }


    fun getData(){

        db.collection("Places").addSnapshotListener { value, error ->

            if (value != null){
                val documents = value.documents
                for (document in documents){
                    val place = document.toObject(Places::class.java)

                    placeList.add(Places(place!!.id, place!!.name, place!!.land, place!!.beskrivning, place!!.lat, place!!.long, place!!.userEmail, place!!.image ))
                }
               listViewAdapter.notifyDataSetChanged()
            }
        }
    }
}

