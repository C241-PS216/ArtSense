package com.harish.artsense.Api.Response

data class LoginData(
    val name: String,
    val token: String,
    val userId: String,
    val isLogin: Boolean,
    val isGuest: Boolean
)
