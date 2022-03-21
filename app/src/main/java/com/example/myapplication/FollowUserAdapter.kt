package com.example.myapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.ItemFollowUserBinding

class FollowUserAdapter(private val listUser: List<User?>?): RecyclerView.Adapter<FollowUserAdapter.ListViewHolder>() {

    class ListViewHolder(var binding:ItemFollowUserBinding ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowUserAdapter.ListViewHolder {
        val binding = ItemFollowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FollowUserAdapter.ListViewHolder(binding)
    }

    override fun getItemCount(): Int = listUser?.size!!

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val user = listUser?.get(position)
        Glide.with(holder.itemView.context)
            .load(user?.avatarUrl)
            .circleCrop()
            .into(holder.binding.imageViewListUser)
        holder.binding.textViewName.text = user?.login
    }

}