package com.harish.artsense.Api.Response

import com.google.gson.annotations.SerializedName

data class HistoryResponse(

	@field:SerializedName("HistoryResponse")
	val historyResponse: List<HistoryResponseItem?>? = null
)

data class HistoryResponseItem(

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("history")
	val history: History? = null
)

data class History(

	@field:SerializedName("result")
	val result: String? = null,

	@field:SerializedName("gambar")
	val gambar: String? = null
)
