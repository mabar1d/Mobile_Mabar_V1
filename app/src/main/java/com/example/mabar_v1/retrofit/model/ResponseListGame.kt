package com.example.mabar_v1.retrofit.model

import com.google.gson.annotations.SerializedName

data class ResponseListGame(

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("desc")
	val desc: String? = null
)

data class DataItem(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("title")
	val title: String? = null
)
