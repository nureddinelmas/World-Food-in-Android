package com.se.iths.app21.grupp1.myapplication.model

import android.text.format.DateFormat
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.DocumentId

data class Places (@DocumentId val id :String,
                   var name : String? = "",
                   var land: String? = "",
                   var beskrivning: String? = "",
                   var lat : Double? = 0.0,
                   var long: Double?= 0.0,
                   var userEmail : String? = "",
                   var image : String? = "" )

{
    constructor() : this ("", "", "", "", 0.0, 0.0, "", "")
}

