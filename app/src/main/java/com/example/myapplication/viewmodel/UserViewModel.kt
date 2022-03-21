package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.myapplication.User
import com.example.myapplication.database.UserRepository

class UserViewModel(application: Application):ViewModel() {

    private val mUserRepository:UserRepository = UserRepository(application)

    fun insert(user: User) {
        mUserRepository.insert(user)
    }

    fun delete(user: User) {
        mUserRepository.delete(user)
    }

}