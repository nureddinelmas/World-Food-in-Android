package com.se.iths.app21.grupp1.myapplication

object DataManagerPlacesDescriptions {


    val comments = mutableListOf<Comments>()
    val place = mutableListOf<Place>()

    init {
        createMockDataComments()
        createMockDataPlace()
    }


    fun createMockDataComments() {
        comments.add(Comments("All"))


    }

    fun createMockDataPlace() {
        place.add(Place("Name","Country", "Image" ))
    }
}