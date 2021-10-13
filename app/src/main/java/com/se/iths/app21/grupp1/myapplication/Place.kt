package com.se.iths.app21.grupp1.myapplication

import com.google.firebase.firestore.DocumentId

class Place (@DocumentId var documentId: String? = null,
             var name: String? = null,
             var land: String? = null) {
}