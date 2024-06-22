package com.harish.artsense.Api.Response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

	@field:SerializedName("userId")
	val userId: String? = null,

	@field:SerializedName("username")
	val username: String? = null
)
