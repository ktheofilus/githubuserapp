package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.preferences.SettingPreferences

class ViewModelFactory constructor(private val mApplication: Application?, private val pref: SettingPreferences?) :
    ViewModelProvider.NewInstanceFactory() {


    constructor(pref: SettingPreferences) : this(null, pref)
    constructor(mApplication: Application) : this(mApplication, null)

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return mApplication?.let { UserViewModel(it) } as T
        } else if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return mApplication?.let { MainViewModel(it) } as T
        } else if (modelClass.isAssignableFrom(NightmodeViewModel::class.java)) {
            return pref?.let { NightmodeViewModel(it) } as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(application: Application): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(application)
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }

}