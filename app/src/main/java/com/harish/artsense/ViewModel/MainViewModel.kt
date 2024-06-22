package com.harish.artsense.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.harish.artsense.Api.Response.LoginData
import com.harish.artsense.di.UserRepository

class MainViewModel(private val repository: UserRepository) : ViewModel() {
    fun getSession(): LiveData<LoginData> {
        return repository.getSession().asLiveData()
    }

    suspend fun logout(){
        repository.logout()
    }
}