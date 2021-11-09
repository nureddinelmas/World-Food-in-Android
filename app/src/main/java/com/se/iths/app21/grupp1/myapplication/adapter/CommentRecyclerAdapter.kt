package com.se.iths.app21.grupp1.myapplication.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.se.iths.app21.grupp1.myapplication.R
import com.se.iths.app21.grupp1.myapplication.databinding.CommentRowLayoutBinding
import com.se.iths.app21.grupp1.myapplication.gone
import com.se.iths.app21.grupp1.myapplication.model.Comments


class CommentRecyclerAdapter(private val commentList: ArrayList<Comments>) : RecyclerView.Adapter<CommentRecyclerAdapter.CommentHolder>(){

    class CommentHolder(val binding: CommentRowLayoutBinding) :RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentHolder {
        val binding = CommentRowLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return CommentHolder(binding)
    }

    override fun onBindViewHolder(holder: CommentHolder, position: Int) {

            holder.binding.commentsText.text = commentList[position].comment
            holder.binding.commentNameText.text = commentList[position].name
        if(commentList[position].profileImage != null || commentList[position].profileImage != "no_image"){
            Glide.with(holder.itemView.context).load(R.drawable.ic_baseline_person).into(holder.binding.selectImagePlaces)
        }
            Glide.with(holder.itemView.context).load(commentList[position].profileImage).into(holder.binding.selectImagePlaces)
        if(commentList[position].rating != ""){
            holder.binding.ratingBarComment.rating = commentList[position].rating.toString().toFloat()
        }else{
            holder.binding.ratingBarComment.gone()
        }

    }

    override fun getItemCount() = commentList.size

}