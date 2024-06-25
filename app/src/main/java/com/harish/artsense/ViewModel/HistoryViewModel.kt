package com.harish.artsense.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.harish.artsense.Api.Response.HistoryResponse
import com.harish.artsense.Api.Response.HistoryResponseItem
import com.harish.artsense.di.ResultState
import com.harish.artsense.di.UserRepository

class HistoryViewModel(private val repository: UserRepository): ViewModel() {
    val Story : LiveData<PagingData<HistoryResponseItem>> =
        repository.getHistory().cachedIn(viewModelScope)
}