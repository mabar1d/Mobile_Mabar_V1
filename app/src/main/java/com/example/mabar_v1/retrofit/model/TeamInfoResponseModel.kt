package com.example.mabar_v1.retrofit.model


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class TeamInfoResponseModel(
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
        var id: Int,
        @SerializedName("image")
        @Expose
        var image: String,
        @SerializedName("info")
        @Expose
        var info: String,
        @SerializedName("name")
        @Expose
        var name: String,
        @SerializedName("personnel")
        @Expose
        var personnel: String
    )
}