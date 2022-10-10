package com.circle.circle_games.retrofit.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


data class ResponseListGame(

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("desc")
	val desc: String? = null
)
@Entity(tableName = "table_game")
data class DataItem(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@PrimaryKey(autoGenerate = false)
	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("title")
	val title: String? = null
)
