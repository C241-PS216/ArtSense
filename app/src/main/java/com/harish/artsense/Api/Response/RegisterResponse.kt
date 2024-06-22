package com.harish.artsense.Api.Response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("error")
	val error: String? = null,

	@field:SerializedName("userId")
	val userId: String? = null,

	@field:SerializedName("username")
	val username: String? = null
)
