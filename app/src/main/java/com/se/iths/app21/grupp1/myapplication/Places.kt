package com.se.iths.app21.grupp1.myapplication

import android.text.format.DateFormat
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.DocumentId

data class Places (@DocumentId val id :String,
                   var name : String? = null,
                   var land: String? = null,
                   var beskrivning: String? = null,
                   var lat : Double? = 0.0,
                   var long: Double?= 0.0,
                   var userEmail : String? = null,
                   var image : String? = null ){
}