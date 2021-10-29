package com.se.iths.app21.grupp1.myapplication.model

import android.widget.ImageView
import com.google.firebase.firestore.DocumentId

class Place (
        var name : String? = null,
        var land : String? = null,
        var image : String? = null
        )
{
        constructor() : this ("", "", "")
}



