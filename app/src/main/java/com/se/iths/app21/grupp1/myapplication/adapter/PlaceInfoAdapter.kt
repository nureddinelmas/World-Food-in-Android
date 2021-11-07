package com.se.iths.app21.grupp1.myapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.se.iths.app21.grupp1.myapplication.R
import com.se.iths.app21.grupp1.myapplication.model.Places
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_places.*

class PlaceInfoAdapter (val context: Context) : GoogleMap.InfoWindowAdapter{

    val layoutInflater = LayoutInflater.from(context)

    override fun getInfoWindow(marker: Marker): View? {
       return null
    }

    override fun getInfoContents(marker: Marker): View? {
        val infoWindow = layoutInflater.inflate(R.layout.info_window, null)

        val placeImage = infoWindow.findViewById<ImageView>(R.id.placeImageView)
        val textName = infoWindow.findViewById<TextView>(R.id.textName)
        val textLand = infoWindow.findViewById<TextView>(R.id.textLand)

        if (marker.tag != null){
            val place = marker!!.tag as Places
            textName.text = place.name
            textLand.text = place.land

            Glide.with(this.context).load(place.image.toString()).into(placeImage)


        }else{
            textName.text = "Vill du spara den här platsen ?"
            textLand.visibility = View.GONE
            placeImage.visibility = View.GONE

        }



        return infoWindow
    }



}