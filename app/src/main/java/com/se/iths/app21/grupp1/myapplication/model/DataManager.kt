package com.se.iths.app21.grupp1.myapplication

import com.se.iths.app21.grupp1.myapplication.model.Cuisine


object Datamanager {
    val cuisines = mutableListOf<Cuisine>()

    init {
        createMockData()
    }


    fun createMockData() {
        cuisines.add(Cuisine("All"))
        cuisines.add(Cuisine("Italian"))
        cuisines.add(Cuisine("American"))
        cuisines.add(Cuisine("Turkish"))

    }
}