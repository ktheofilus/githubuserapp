package com.example.myapplication.database

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.myapplication.User
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class UserRepository(application: Application) {

    private val mUserDao :UserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()


    init {
        val db = UserDatabase.getDatabase(application)
        mUserDao = db.userDao()
    }

    fun getAllUsers():LiveData<List<User>> = mUserDao.getAll()

    fun insert(user: User) {
        executorService.execute { mUserDao.insert(user) }
    }

    fun delete(user: User) {
        executorService.execute { mUserDao.delete(user) }
    }

     suspend fun bookmark(id:String):Boolean {
        return mUserDao.bookmark(id)
    }

}