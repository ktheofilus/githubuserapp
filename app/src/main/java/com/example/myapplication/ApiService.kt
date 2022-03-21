package com.example.myapplication

import retrofit2.Call
import retrofit2.http.*

interface ApiService {


    @GET("/search/users")
    fun getUser(
        @Query("q") id: String,
    ): Call<UserResponse>

    @GET("/users/{id}/followers")
    fun getFollowers(
        @Path("id") id: String,

    ): Call<List<User>>

    @GET("/users/{id}/following")
    fun getFollowing(
        @Path("id") id: String,
    ): Call<List<User>>

    @GET("/users/{id}")
    fun getDetailUser(
        @Path("id") id: String,
    ): Call<DetailUser>
}