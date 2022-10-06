package com.circle.circle_games.retrofit.model

import com.google.gson.annotations.SerializedName

data class ResponseGetInfoTournamentModel(

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("desc")
	val desc: String? = null
)

data class Data(

	@field:SerializedName("end_date")
	val endDate: String? = null,

	@field:SerializedName("created_name")
	val createdName: String? = null,

	@field:SerializedName("image")
	val image: Any? = null,

	@field:SerializedName("number_of_participants")
	val numberOfParticipants: Int? = null,

	@field:SerializedName("prize")
	val prize: String? = null,

	@field:SerializedName("register_date_start")
	val registerDateStart: String? = null,

	@field:SerializedName("id_created_by")
	val idCreatedBy: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("register_date_end")
	val registerDateEnd: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("detail")
	val detail: String? = null,

	@field:SerializedName("start_date")
	val startDate: String? = null,

	@field:SerializedName("game_id")
	val gameId: String? = null,

	@field:SerializedName("title_game")
	val titleGame: String? = null,

	@field:SerializedName("register_fee")
	val register_fee: String? = null,

	@field:SerializedName("rating")
	val rating: String? = null
)
