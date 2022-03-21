package com.example.myapplication


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.ItemRowUserBinding
import com.example.myapplication.viewmodel.MainViewModel
import com.example.myapplication.viewmodel.UserViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class ListUserAdapter(private var listUser: List<User?>?,private var owner: LifecycleOwner,private val userViewModel: UserViewModel,private val mainViewModel: MainViewModel): RecyclerView.Adapter<ListUserAdapter.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback
    private var lastPosition:Int = -1
    private lateinit var context: Context

    interface OnItemClickCallback {
        fun onItemClicked(data: User?)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ListViewHolder(var binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {

        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context=parent.context

        return ListViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        var user = listUser?.get(position)

        setAnimation(holder.itemView, position)

        Glide.with(holder.itemView.context)
            .load(user?.avatarUrl)
            .circleCrop()
            .into(holder.binding.imageViewListUser)
        holder.binding.textViewUsername.text = user?.login

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(user)
        }

        mainViewModel.bookmark.observe(owner){

                runBlocking {
                    launch {
                        if(user?.let { it1 -> mainViewModel.mUserRepository.bookmark(it1.login) } == true){
                            user.bookmark =true
                            holder.binding.bookmark.setImageResource(R.drawable.ic_baseline_bookmark_24)
                        }
                        else{
                            user?.bookmark=false
                            holder.binding.bookmark.setImageResource(R.drawable.ic_baseline_bookmark_border_24)
                        }
                    }
                }
        }

        holder.binding.bookmark.setOnClickListener{

            if (!user!!.bookmark!!){
                user.bookmark=true
                userViewModel.insert(user)

            }else{
                userViewModel.delete(user)
                user.bookmark=false
                holder.binding.bookmark.setImageResource(R.drawable.ic_baseline_bookmark_border_24)
            }
        }
    }

    private fun setAnimation(viewToAnimate: View, position: Int) {
        if (position > lastPosition)
        {
            val animation :Animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in)
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        }
    }

    override fun getItemCount(): Int = listUser?.size!!



}



