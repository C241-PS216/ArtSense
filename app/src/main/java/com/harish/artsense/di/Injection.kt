package com.harish.artsense.di

import android.content.Context
import com.harish.artsense.Api.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking


object Injection {
    fun provideRepository(context: Context): UserRepository {
        val preferences = DataPreference.getInstance(context.dataStore)
        val user = runBlocking { preferences.getSession().first()}
        val apiService = ApiConfig.getApiService(user.token)
        return UserRepository.getInstance(apiService,preferences)
    }
}