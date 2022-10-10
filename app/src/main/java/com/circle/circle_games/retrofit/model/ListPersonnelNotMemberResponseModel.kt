package com.circle.circle_games.retrofit.model


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class ListPersonnelNotMemberResponseModel(
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
        @SerializedName("username")
        @Expose
        var username: String,
        @SerializedName("user_id")
        @Expose
        var userId: Int,

        var onClick: Boolean = false
    )
}