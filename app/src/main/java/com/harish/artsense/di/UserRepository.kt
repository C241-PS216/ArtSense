package com.harish.artsense.di

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.harish.artsense.Api.ApiService
import com.harish.artsense.Api.Response.LoginData
import com.harish.artsense.Api.Response.LoginResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.io.IOException

class UserRepository(  private val apiService: ApiService,private val dataPreference: DataPreference) {



    fun login(username: String, password: String) : LiveData<ResultState<LoginResponse>> = liveData {
        emit(ResultState.Loading)
        try {
            val successResult = apiService.login(username, password)
            emit(ResultState.Success(successResult))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, LoginResponse::class.java)
            emit(ResultState.Error(errorResponse.error!!))
        }
    }

    suspend fun saveSession(loginResponse: LoginResponse){
        return dataPreference.saveSession(loginResponse)
    }
    fun getSession(): Flow<LoginData> {
        return dataPreference.getSession()
    }
    suspend fun logout(){
        dataPreference.logout()
    }

     suspend fun guestLogin(login : LoginData) {
        return dataPreference.guestLogin(login)
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(apiService: ApiService, dataPreference: DataPreference) = UserRepository(apiService, dataPreference)

    }
}
