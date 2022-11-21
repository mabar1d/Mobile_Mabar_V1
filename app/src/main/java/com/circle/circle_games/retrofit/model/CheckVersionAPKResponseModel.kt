package com.circle.circle_games.retrofit.model


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class CheckVersionAPKResponseModel(
    @SerializedName("code")
    @Expose
    val code: String?,
    @SerializedName("data")
    @Expose
    val `data`: List<Data?>?,
    @SerializedName("desc")
    @Expose
    val desc: String?
) {
    data class Data(
        @SerializedName("id")
        @Expose
        val id: Int?,
        @SerializedName("type")
        @Expose
        val type: String?,
        @SerializedName("version")
        @Expose
        val version: String?
    )
}