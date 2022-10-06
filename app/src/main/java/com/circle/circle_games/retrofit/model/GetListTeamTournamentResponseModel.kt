package com.circle.circle_games.retrofit.model


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class GetListTeamTournamentResponseModel(
    @SerializedName("code")
    @Expose
    var code: String,
    @SerializedName("data")
    @Expose
    var `data`: List<Data>,
    @SerializedName("desc")
    @Expose
    var desc: String
) {
    data class Data(
        @SerializedName("tournament_detail")
        @Expose
        var tournamentDetail: String,
        @SerializedName("tournament_end_date")
        @Expose
        var tournamentEndDate: String,
        @SerializedName("tournament_id")
        @Expose
        var tournamentId: Int,
        @SerializedName("tournament_image")
        @Expose
        var tournamentImage: String,
        @SerializedName("tournament_last_position")
        @Expose
        var tournamentLastPosition: String,
        @SerializedName("tournament_name")
        @Expose
        var tournamentName: String,
        @SerializedName("tournament_prize")
        @Expose
        var tournamentPrize: String,
        @SerializedName("tournament_start_date")
        @Expose
        var tournamentStartDate: String
    )
}