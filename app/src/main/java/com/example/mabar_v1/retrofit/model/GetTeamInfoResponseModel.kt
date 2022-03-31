package com.example.mabar_v1.retrofit.model


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class GetTeamInfoResponseModel(
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
        @SerializedName("admin_id")
        @Expose
        var adminId: String,
        @SerializedName("id")
        @Expose
        var id: String,
        @SerializedName("image")
        @Expose
        var image: String,
        @SerializedName("info")
        @Expose
        var info: String,
        @SerializedName("name")
        @Expose
        var name: String,
        @SerializedName("game_id")
        @Expose
        var game_id: String,
        @SerializedName("title_game")
        @Expose
        var title_game: String,
        @SerializedName("personnel")
        @Expose
        var personnel: List<Personnel>,
        @SerializedName("username_admin")
        @Expose
        var usernameAdmin: String
    ) {
        data class Personnel(
            @SerializedName("user_id")
            @Expose
            var userId: String,
            @SerializedName("username")
            @Expose
            var username: String
        )
    }
}