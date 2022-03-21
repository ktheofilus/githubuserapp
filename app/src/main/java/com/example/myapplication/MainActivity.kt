package com.example.myapplication

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.preferences.SettingPreferences
import com.example.myapplication.viewmodel.MainViewModel
import com.example.myapplication.viewmodel.NightmodeViewModel
import com.example.myapplication.viewmodel.UserViewModel
import com.example.myapplication.viewmodel.ViewModelFactory
import com.google.android.material.snackbar.Snackbar

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var mainViewModel: MainViewModel
    private lateinit var nightmodeViewModel: NightmodeViewModel


    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        supportActionBar?.title = resources.getString(R.string.mainTitle)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progressBar.visibility = View.GONE

        val pref = SettingPreferences.getInstance(dataStore)
        val factory = ViewModelFactory(this.application)
        val preffactory = ViewModelFactory(pref)

        nightmodeViewModel = ViewModelProvider(this, preffactory)[NightmodeViewModel::class.java]
        userViewModel= ViewModelProvider(this,factory)[UserViewModel::class.java]
        mainViewModel = ViewModelProvider(this,factory)[MainViewModel::class.java]

        nightmodeViewModel.getThemeSettings().observe(this
        ) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = binding.userSearchView
        searchView.hasWindowFocus()

        mainViewModel.snackbarText.observe(this) {
            it.getContentIfNotHandled()?.let { snackBarText ->
                Snackbar.make(
                    binding.root,
                    snackBarText,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        mainViewModel.isLoading.observe(this) {
            mainViewModel.showLoading(it, binding.progressBar)
        }

        mainViewModel.users.observe(this) {
            showRecyclerList(it)
        }

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                mainViewModel.findUser(query)
                searchView.clearFocus()
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.mainmenu, menu)
        return true
    }

    override fun onResume() {
        super.onResume()
        binding.userSearchView.clearFocus()
    }

    private fun showRecyclerList(users: List<User?>?) {
        binding.recycleView.layoutManager= LinearLayoutManager(this)
        val listUserAdapter = ListUserAdapter(users,this,userViewModel,mainViewModel)
        binding.recycleView.adapter = listUserAdapter
        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User?) {
                val detailUserIntent = Intent(this@MainActivity, DetailUserActivity::class.java)
                detailUserIntent.putExtra(DetailUserActivity.EXTRA_USER,data)
                startActivity(detailUserIntent)
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.bookmark -> {
                val bookmarkIntent = Intent(this@MainActivity, BookmarkActivity::class.java)
                startActivity(bookmarkIntent)
            }
            R.id.nightmode -> {
                when (resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
                    Configuration.UI_MODE_NIGHT_YES -> {nightmodeViewModel.saveThemeSetting(false)}
                    Configuration.UI_MODE_NIGHT_NO -> {nightmodeViewModel.saveThemeSetting(true)}
                    Configuration.UI_MODE_NIGHT_UNDEFINED -> {nightmodeViewModel.saveThemeSetting(true)}
                }
            }
        }
        return true
    }
}


