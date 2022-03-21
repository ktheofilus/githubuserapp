package com.example.myapplication.viewmodel

import android.app.Application
import android.view.View
import android.widget.ProgressBar
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.*
import com.example.myapplication.database.UserRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(application: Application) : ViewModel() {

    private val _users = MutableLiveData<List<User?>?>()
    val users: LiveData<List<User?>?> = _users

    private val _follower = MutableLiveData<List<User?>?>()
    val follower: LiveData<List<User?>?> = _follower

    private val _following = MutableLiveData<List<User?>?>()
    val following: LiveData<List<User?>?> = _following

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _user = MutableLiveData<DetailUser>()
    val user: LiveData<DetailUser> = _user

    private val _isLoadingDetail = MutableLiveData<Boolean>()
    val isLoadingDetail: LiveData<Boolean> = _isLoadingDetail

    private val _isLoadingFollowing = MutableLiveData<Boolean>()
    val isLoadingFollowing: LiveData<Boolean> = _isLoadingFollowing

    private val _isLoadingFollowers = MutableLiveData<Boolean>()
    val isLoadingFollowers: LiveData<Boolean> = _isLoadingFollowers

    private val _snackbarText = MutableLiveData<Event<String>>()
    val snackbarText: LiveData<Event<String>> = _snackbarText

    var tempUser: String = ""

    fun setUser(name: String) {
        tempUser = name
    }

    val mUserRepository: UserRepository = UserRepository(application)


    fun getAllUsers(): LiveData<List<User>> = mUserRepository.getAllUsers()

    val bookmark: LiveData<List<User>> = getAllUsers()

    fun findUser(id: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUser(id)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {

                    val tempUsers = response.body()?.items

                    val usersList = ArrayList<User>()

                    runBlocking {
                        launch {
                            tempUsers?.forEach { user ->
                                val isBookmarked = mUserRepository.bookmark(user?.login!!)

                                val tempUser = User(
                                    user.login,
                                    user.avatarUrl,
                                    isBookmarked
                                )
                                usersList.add(tempUser)
                            }
                        }
                    }

                    _users.value = usersList

                    if (_users.value?.size == 0) {
                        _snackbarText.value = Event("User not Found")
                    }
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _isLoading.value = false
                _snackbarText.value = Event(t.message.toString())
            }
        })
    }

    fun getFollowers(id: String) {
        _isLoadingFollowers.value = true
        val client = ApiConfig.getApiService().getFollowers(id)
        client.enqueue(object : Callback<List<User>> {
            override fun onResponse(
                call: Call<List<User>>,
                response: Response<List<User>>
            ) {
                _isLoadingFollowers.value = false
                if (response.isSuccessful) {
                    _follower.value = response.body()
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                _snackbarText.value = Event(t.message.toString())
                _isLoadingFollowers.value = false

            }
        })
    }


    fun getFollowing(id: String) {
        _isLoadingFollowing.value = true

        val client = ApiConfig.getApiService().getFollowing(id)
        client.enqueue(object : Callback<List<User>> {
            override fun onResponse(
                call: Call<List<User>>,
                response: Response<List<User>>
            ) {
                _isLoadingFollowing.value = false

                if (response.isSuccessful) {
                    _following.value = response.body()
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                _snackbarText.value = Event(t.message.toString())
                _isLoadingFollowing.value = false

            }
        })
    }

    fun getDetailUser(id: String) {
        _isLoadingDetail.value = true
        val client = ApiConfig.getApiService().getDetailUser(id)
        client.enqueue(object : Callback<DetailUser> {
            override fun onResponse(
                call: Call<DetailUser>,
                response: Response<DetailUser>
            ) {
                if (response.isSuccessful) {
                    _user.value = response.body()

                    _isLoadingDetail.value = false

                }
            }

            override fun onFailure(call: Call<DetailUser>, t: Throwable) {
                _isLoadingDetail.value = false
                _snackbarText.value = Event(t.message.toString())
            }

        })
    }

    fun showLoading(isLoading: Boolean, progressBar: ProgressBar) {
        if (isLoading) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

}


