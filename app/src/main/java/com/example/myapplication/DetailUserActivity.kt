package com.example.myapplication

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.ActivityDetailUserBinding
import com.example.myapplication.viewmodel.MainViewModel
import com.example.myapplication.viewmodel.ViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class DetailUserActivity : AppCompatActivity(){

    private lateinit var binding: ActivityDetailUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_user)

        supportActionBar?.title = resources.getString(R.string.detailTitle)

        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progressBar.visibility = View.GONE


        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        val factory = ViewModelFactory(this.application)
        val viewModel = ViewModelProvider(this,factory).get(
            MainViewModel::class.java
        )
        val user = intent.getParcelableExtra<User>(EXTRA_USER)

        viewModel.isLoadingDetail.observe(LifecycleOwner { lifecycle }) {
            viewModel.showLoading(it, binding.progressBar)
        }


        viewModel.snackbarText.observe(this) {
            it.getContentIfNotHandled()?.let { snackBarText ->
                Snackbar.make(
                    binding.root,
                    snackBarText,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        viewModel.run { user?.login?.let { getDetailUser(it) } }
        user?.login?.let { viewModel.setUser(it) }

        viewModel.user.observe(LifecycleOwner { lifecycle }){
                Glide.with(this)
                    .load(it.avatarUrl)
                    .circleCrop()
                    .into(binding.imageView)


            if (it.name!=null)  binding.textViewName.text= it.name else binding.textViewName.visibility = View.GONE
            binding.textViewUsername.text= it.login
            val follower = it.followers
            val following = it.following
            binding.textViewFollow.text = resources.getString(R.string.follow, follower, following)
            if (it.company!=null)  binding.textViewCompany.text= it.company else {
                binding.textViewCompany.visibility = View.GONE
                binding.imageView4.visibility = View.GONE
            }
            if (it.location!=null)  binding.textViewLocation.text= it.location else {
                binding.textViewLocation.visibility = View.GONE
                binding.imageView5.visibility = View.GONE
            }
            binding.textViewRepository.text= it.publicRepos.toString() + " Repositories"
        }

    }

    companion object{
        const val EXTRA_USER="extra_user"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }


}