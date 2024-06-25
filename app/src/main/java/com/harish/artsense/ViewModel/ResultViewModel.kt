package com.harish.artsense.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.harish.artsense.Api.Response.ArtistResponse
import com.harish.artsense.di.ResultState
import com.harish.artsense.di.UserRepository

class ResultViewModel(private val repository: UserRepository) : ViewModel() {
    fun getArtist(nama : String) : LiveData<ResultState<ArtistResponse>> {
     return repository.getArtist(nama)
    }
}