package com.harish.artsense.Api.Response

import com.google.gson.annotations.SerializedName

data class ArtistResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("error")
	val error: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("links")
	val links: List<String?>? = null
)
