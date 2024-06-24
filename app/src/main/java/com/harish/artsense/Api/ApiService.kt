package com.harish.artsense.Api

import com.harish.artsense.Api.Response.HistoryResponse
import com.harish.artsense.Api.Response.LoginResponse
import com.harish.artsense.Api.Response.RegisterResponse
import com.harish.artsense.Api.Response.UploadResponse
import okhttp3.MultipartBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {
            @POST("register")
            @FormUrlEncoded
            suspend fun register(
                @Field("username") email: String,
                @Field("password") password: String
            ) : RegisterResponse

            @POST("login")
            @FormUrlEncoded
            suspend fun login(
                @Field("username") email: String,
                @Field("password") password: String,
            ) : LoginResponse

            @GET("history")
            suspend fun getHistory(
                @Query("page") page: Int = 1,
                @Query("size") size: Int = 20
            ): HistoryResponse

            @Multipart
            @POST("upload")
            suspend fun uploadImage(
                @Part file: MultipartBody.Part,
            ): UploadResponse

}