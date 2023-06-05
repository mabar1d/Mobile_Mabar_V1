package com.circle.circle_games.retrofit.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ResponseGetInfoTournamentModel(

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("desc")
	val desc: String? = null
)

{
	data class Data(
			@SerializedName("created_name")
			@Expose
			val createdName: String? = "",
			@SerializedName("detail")
			@Expose
			val detail: String? = "",
			@SerializedName("end_date")
			@Expose
			val endDate: String? = "",
			@SerializedName("game_id")
			@Expose
			val gameId: String? = "",
			@SerializedName("id")
			@Expose
			val id: String? = "",
			@SerializedName("id_created_by")
			@Expose
			val idCreatedBy: String? = "",
			@SerializedName("image")
			@Expose
			val image: String? = "",
			@SerializedName("name")
			@Expose
			val name: String? = "",
			@SerializedName("number_of_participants")
			@Expose
			val numberOfParticipants: String? = "",
			@SerializedName("prize")
			@Expose
			val prize: String? = "",
			@SerializedName("rating")
			@Expose
			val rating: String? = "",
			@SerializedName("register_date_end")
			@Expose
			val registerDateEnd: String? = "",
			@SerializedName("register_date_start")
			@Expose
			val registerDateStart: String? = "",
			@SerializedName("register_fee")
			@Expose
			val registerFee: String? = "",
			@SerializedName("start_date")
			@Expose
			val startDate: String? = "",
			@SerializedName("team_in_tournament")
			@Expose
			val teamInTournament: List<TeamInTournament?>? = listOf(),
			@SerializedName("terms_condition")
			@Expose
			val termsCondition: String? = "",
			@SerializedName("title_game")
			@Expose
			val titleGame: String? = "",
			@SerializedName("type")
			@Expose
			val type: String? = ""
	) {
		data class TeamInTournament(
				@SerializedName("team_id")
				@Expose
				val teamId: Int? = 0,
				@SerializedName("team_name")
				@Expose
				val teamName: String? = ""
		)
	}
}
