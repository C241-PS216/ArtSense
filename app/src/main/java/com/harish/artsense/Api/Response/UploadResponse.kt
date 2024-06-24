package com.harish.artsense.Api.Response

import com.google.gson.annotations.SerializedName

data class UploadResponse(

	@field:SerializedName("error")
	val error: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null
)
