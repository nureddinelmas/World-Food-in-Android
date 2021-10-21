package com.se.iths.app21.grupp1.myapplication

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class CuisinesRecycleAdapter(val context: Context) :
    RecyclerView.Adapter<CuisinesRecycleAdapter.ViewHolder>() {
    val cuisines: MutableList<Cuisine> = mutableListOf<Cuisine>()

    val layoutInflater = LayoutInflater.from(context)

    var selectedCountries: MutableList<String> = mutableListOf<String>()

    var CuisineSelectListener: CuisineSelectListener? = null






    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = layoutInflater.inflate(R.layout.list_cuisines, parent, false)
        return ViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cuisines = cuisines[position]

        holder.countryCuisineTextView.text = cuisines.country


        if(selectedCountries.isEmpty() && position == 0){
            //select all

            holder.checkBox.setChecked(true)
        } else if (selectedCountries.contains(cuisines.country)){
            holder.checkBox.setChecked(true)
        } else{
            holder.checkBox.setChecked(false)
        }

        holder.itemView.setOnClickListener {
            Log.d("size", selectedCountries.size.toString())

            //selectedCountries.add(cuisines.country)

            if(selectedCountries.contains(cuisines.country)){
                selectedCountries.remove(cuisines.country)
            }else{
                selectedCountries.add(cuisines.country)
            }

            if(selectedCountries.contains(cuisines.country)){
                holder.checkBox.setChecked(true)
            } else{
                holder.checkBox.setChecked(false)
            }


            CuisineSelectListener?.onSelectCuisine()


        }


        }




    override fun getItemCount() = cuisines.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val countryCuisineTextView = itemView.findViewById<TextView>(R.id.countryCuisineTextView)
        var cuisinePosition = 0

        var checkBox = itemView.findViewById<CheckBox>(R.id.checkBox)



    }

    fun containsCountry(countryName:String) : Boolean{
        var contains = cuisines.any{
            it.country == countryName
        }
        return contains
    }

    fun addCuisine(country: String){
        if(!containsCountry(country)){
            var cuisine = Cuisine(country)
            cuisines.add(cuisine)
        }
    }


}