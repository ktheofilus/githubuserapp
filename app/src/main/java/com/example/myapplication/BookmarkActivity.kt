package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.ActivityBookmarkBinding
import com.example.myapplication.viewmodel.MainViewModel
import com.example.myapplication.viewmodel.UserViewModel
import com.example.myapplication.viewmodel.ViewModelFactory

class BookmarkActivity : AppCompatActivity() {

    private lateinit var binding:ActivityBookmarkBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bookmark)

        binding = ActivityBookmarkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = resources.getString(R.string.bookmarkTitle)


        val factory = ViewModelFactory(this.application)
        userViewModel= ViewModelProvider(this,factory).get(
            UserViewModel::class.java
        )

        mainViewModel = ViewModelProvider(this,factory).get(
            MainViewModel::class.java
        )

        mainViewModel.bookmark.observe(this) {
            showRecyclerList(it)
        }
    }

    private fun showRecyclerList(users: List<User?>?) {
        binding.recyclerView.layoutManager= LinearLayoutManager(this)
        val listUserAdapter = ListUserAdapter(users,this,userViewModel,mainViewModel)
        binding.recyclerView.adapter = listUserAdapter
        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User?) {
                val detailUserIntent = Intent(this@BookmarkActivity, DetailUserActivity::class.java)
                detailUserIntent.putExtra(DetailUserActivity.EXTRA_USER,data)
                startActivity(detailUserIntent)
            }
        })
    }

}