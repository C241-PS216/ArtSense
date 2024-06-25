package com.harish.artsense.Api.Response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class UploadResponse(
	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("error")
	val error: String? = null,

	@field:SerializedName("artist")
	val artist: Artist? = null,

	@field:SerializedName("history")
	val history: History? = null,

	@field:SerializedName("url")
	val url: String? = null
)

data class Artist(

	@field:SerializedName("nama")
	val nama: String? = null
)

data class History(

	@field:SerializedName("result")
	val result: String? = null,

	@field:SerializedName("gambar")
	val gambar: String? = null,

	@field:SerializedName("timestamp")
	val timestamp: String? = null
)
