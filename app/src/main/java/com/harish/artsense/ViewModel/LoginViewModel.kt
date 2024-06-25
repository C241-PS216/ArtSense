package com.harish.artsense.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harish.artsense.Api.Response.LoginResponse
import com.harish.artsense.di.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel(){
    fun login(username: String, password :String) = repository.login(username, password)


    fun saveSession(loginResult: LoginResponse){
        viewModelScope.launch {
            repository.saveSession(loginResult)
        }
    }

}

