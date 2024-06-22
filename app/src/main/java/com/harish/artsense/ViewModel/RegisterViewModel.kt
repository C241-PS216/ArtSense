package com.harish.artsense.ViewModel

import androidx.lifecycle.ViewModel
import com.harish.artsense.di.UserRepository

class RegisterViewModel(private val repository: UserRepository) : ViewModel() {
    fun register(username : String, password :String) = repository.register(username, password)

}