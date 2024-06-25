package com.harish.artsense.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.harish.artsense.Api.Response.LoginData
import com.harish.artsense.di.UserRepository
import java.io.File

class MainViewModel(private val repository: UserRepository) : ViewModel() {
    fun getSession(): LiveData<LoginData> {
        return repository.getSession().asLiveData()
    }
    fun Upload(file: File) = repository.Upload(file)

    suspend fun logout(){
        repository.logout()
    }
}