package com.harish.artsense.di

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.google.gson.Gson
import com.harish.artsense.Api.ApiService
import com.harish.artsense.Api.Response.ArtistResponse
import com.harish.artsense.Api.Response.HistoryPagingSource
import com.harish.artsense.Api.Response.HistoryResponseItem
import com.harish.artsense.Api.Response.LoginData
import com.harish.artsense.Api.Response.LoginResponse
import com.harish.artsense.Api.Response.RegisterResponse
import com.harish.artsense.Api.Response.UploadResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import java.io.File

class UserRepository(  private val apiService: ApiService,private val dataPreference: DataPreference) {




    fun getArtist(name : String) : LiveData<ResultState<ArtistResponse>> = liveData{
        emit(ResultState.Loading)
        try {
            val successReult = apiService.getArtist(name)
            emit(ResultState.Success(successReult))
        }catch (e : HttpException){
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ArtistResponse::class.java)
            emit(ResultState.Error(errorResponse.error!!))
        }
    }

    fun Upload(imageFile: File): LiveData<ResultState<UploadResponse>>  = liveData {
        emit(ResultState.Loading)
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "file",
            imageFile.name,
            requestImageFile
        )
        try {
            val successResponse = apiService.uploadImage(multipartBody)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, UploadResponse::class.java)
            emit(ResultState.Error(errorResponse.error!!))
        }

    }

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

    fun register(username: String, password: String) : LiveData<ResultState<RegisterResponse>> = liveData {
        emit(ResultState.Loading)
        try {
            val successResult = apiService.register(username, password)
            emit(ResultState.Success(successResult))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, LoginResponse::class.java)
            emit(ResultState.Error(errorResponse.error!!))
        }
    }

    fun getHistory(): LiveData<PagingData<HistoryResponseItem>>{
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                HistoryPagingSource(apiService)
            }
        ).liveData
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
