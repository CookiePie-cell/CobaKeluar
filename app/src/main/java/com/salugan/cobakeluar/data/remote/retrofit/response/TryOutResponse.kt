package com.salugan.cobakeluar.data.remote.retrofit.response

import com.google.gson.annotations.SerializedName

data class TryOutResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: List<DataItem>,

	@field:SerializedName("message")
	val message: String? = null
)













