package com.se.iths.app21.grupp1.myapplication.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.RoundedCorner
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.se.iths.app21.grupp1.myapplication.R
import com.se.iths.app21.grupp1.myapplication.gone
import com.se.iths.app21.grupp1.myapplication.model.Places
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_places.*

class PlaceInfoAdapter (val context: Context) : GoogleMap.InfoWindowAdapter{

 //    val layoutInflater = LayoutInflater.from(context)

    override fun getInfoWindow(marker: Marker): View? {
        return null
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun getInfoContents(marker: Marker): View? {
        val infoWindow = LayoutInflater.from(context).inflate(R.layout.info_window, null)

        val placeImage = infoWindow.findViewById<ImageView>(R.id.placeImageView)
        val textName = infoWindow.findViewById<TextView>(R.id.textName)
        val textLand = infoWindow.findViewById<TextView>(R.id.textLand)
        val progressBar = infoWindow.findViewById<ProgressBar>(R.id.progressBar_infoWindow)

        if (marker.tag != null){
            val place = marker!!.tag as Places
            Glide.with(this.context).load(place.image).fitCenter().into(placeImage)
            textName.text = place.name
            textLand.text = place.land


            progressBar.gone()

        }else{
            textName.text = "Vill du spara den här platsen ?"
            textLand.visibility = View.GONE
            placeImage.visibility = View.GONE

        }



        return infoWindow
    }



}