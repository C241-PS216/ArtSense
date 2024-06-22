package com.harish.artsense.Api

import com.harish.artsense.Api.Response.LoginResponse
import com.harish.artsense.Api.Response.RegisterResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

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
}