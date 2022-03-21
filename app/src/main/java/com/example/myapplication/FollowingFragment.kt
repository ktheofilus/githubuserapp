package com.example.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.viewmodel.MainViewModel

class FollowingFragment : Fragment() {

    private lateinit var recyclerView:RecyclerView
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var progressBar:ProgressBar


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView=view.findViewById(R.id.followingRecyclerView)
        recyclerView.layoutManager= LinearLayoutManager(requireActivity())
        progressBar=view.findViewById(R.id.progressBar)
        progressBar.visibility = View.GONE

    }

    override fun onStart() {
        super.onStart()

        viewModel.isLoadingFollowing.observe(LifecycleOwner { lifecycle }, {
            viewModel.showLoading(it,progressBar)
        })

        viewModel.following.observe(this, { following ->
            val listUserAdapter = FollowUserAdapter(following)
            recyclerView.adapter = listUserAdapter
        })

        viewModel.getFollowing(viewModel.tempUser)

    }

}