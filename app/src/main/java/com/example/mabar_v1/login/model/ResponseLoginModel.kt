package com.example.mabar_v1.login.model

import com.google.gson.annotations.SerializedName

data class ResponseLoginModel(

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("desc")
	val desc: String? = null
)

data class User(

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("username")
	val username: String? = null
)

data class Data(

	@field:SerializedName("access_token")
	val accessToken: String? = null,

	@field:SerializedName("token_type")
	val tokenType: String? = null,

	@field:SerializedName("expires_in")
	val expiresIn: String? = null,

	@field:SerializedName("user")
	val user: User? = null
)
