package com.example.myapplication.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.myapplication.User

@Dao
interface UserDao {

    @Query("SELECT * FROM user")
    fun getAll(): LiveData<List<User>>

    @Insert
    fun insert(user: User)

    @Delete
    fun delete(user: User)

    @Query("SELECT EXISTS(SELECT * FROM user WHERE login = :user)")
    suspend fun bookmark(user: String): Boolean

}