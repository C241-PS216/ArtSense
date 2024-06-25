package com.harish.artsense.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.harish.artsense.Api.Response.LoginData
import com.harish.artsense.di.ResultState
import com.harish.artsense.di.UserRepository

class WelcomeViewModel(private val repository: UserRepository) : ViewModel() {

    suspend fun guestLogin(login : LoginData) {
       return repository.guestLogin(login)
    }
}