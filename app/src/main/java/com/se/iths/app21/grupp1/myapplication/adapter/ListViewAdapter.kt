package com.se.iths.app21.grupp1.myapplication.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.se.iths.app21.grupp1.myapplication.databinding.ListRecyclerViewBinding
import com.se.iths.app21.grupp1.myapplication.model.Places
import com.se.iths.app21.grupp1.myapplication.view.MapsActivity

class ListViewAdapter(private var placeList : ArrayList<Places>) : RecyclerView.Adapter<ListViewAdapter.ListHolder>(), Filterable {

    var placeFilterList = ArrayList<Places>()

    init {
        placeFilterList = placeList
    }

    class ListHolder(val binding: ListRecyclerViewBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListHolder {
        val binding =  ListRecyclerViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListHolder(binding)
    }

    override fun onBindViewHolder(holder: ListHolder, position: Int) {
       holder.binding.textNameAndLand.text = placeFilterList[position].name + " / " +placeFilterList[position].land
        Glide.with(holder.itemView.context).load(placeFilterList[position].image.toString()).into(holder.binding.selectImageListView)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, MapsActivity::class.java)
            intent.putExtra("doc", placeFilterList[position].id)
            intent.putExtra("position", "new")
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
       return placeFilterList.size
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(p0: CharSequence?): FilterResults {
                val charSearch = p0.toString().toLowerCase()

                val results = Filter.FilterResults()

                if(charSearch != null && charSearch.isNotEmpty()){
                    val filteredList = ArrayList<Places>()
                    placeList.forEach {
                        if (it.land?.toLowerCase()!!.contains(charSearch) || it.name?.toLowerCase()!!.contains(charSearch)){
                            filteredList.add(it)
                        }
                    }
                    results.values = filteredList
                }else{
                    results.values = placeList
                }
                return results
            }

            override fun publishResults(p0: CharSequence?, results: FilterResults?) {
               placeFilterList = results!!.values as ArrayList<Places>
                notifyDataSetChanged()
            }

        }
    }
}