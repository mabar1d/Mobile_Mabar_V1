package com.example.mabar_v1.retrofit.model


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class ResponseCreateTournamentResponseModel(
    @SerializedName("code")
    @Expose
    var code: String,
    @SerializedName("data")
    @Expose
    var `data`: Data,
    @SerializedName("desc")
    @Expose
    var desc: String
) {
    data class Data(
        @SerializedName("tournament_id")
        @Expose
        var tournamentId: Int
    )
}