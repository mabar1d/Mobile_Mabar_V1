package com.example.mabar_v1.retrofit.model


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class GetListTournamentResponseModel(
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
        @SerializedName("detail")
        @Expose
        var detail: String,
        @SerializedName("end_date")
        @Expose
        var endDate: String,
        @SerializedName("id")
        @Expose
        var id: Int,
        @SerializedName("id_created_by")
        @Expose
        var idCreatedBy: String,
        @SerializedName("name")
        @Expose
        var name: String,
        @SerializedName("number_of_participants")
        @Expose
        var numberOfParticipants: Int,
        @SerializedName("poster")
        @Expose
        var poster: Any,
        @SerializedName("prize")
        @Expose
        var prize: String,
        @SerializedName("register_date_end")
        @Expose
        var registerDateEnd: String,
        @SerializedName("register_date_start")
        @Expose
        var registerDateStart: String,
        @SerializedName("start_date")
        @Expose
        var startDate: String,
        @SerializedName("image")
        @Expose
        var image: String,
        @SerializedName("game_id")
        @Expose
        var game_id: Int,
        @SerializedName("type")
        @Expose
        var type: Int,
        @SerializedName("title_game")
        @Expose
        var title_game: String,
        @SerializedName("rating")
        @Expose
        var rating: String,
        @SerializedName("created_name")
        @Expose
        var created_name: String
    )
}