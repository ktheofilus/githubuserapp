package com.example.myapplication.viewmodel

import android.util.Log
import com.example.myapplication.database.UserRepository
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class MainViewModelTest : TestCase()

    private lateinit var userRepository:UserRepository

@Before
fun before(){
    userRepository = Mockito.mock(UserRepository::class.java)
}

