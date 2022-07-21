package com.example.mabar_v1.retrofit.model


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class GetLinkTreeWebviewResponseModel(
    @SerializedName("code")
    @Expose
    val code: String,
    @SerializedName("data")
    @Expose
    val `data`: Data,
    @SerializedName("desc")
    @Expose
    val desc: String
) {
    data class Data(
        @SerializedName("url")
        @Expose
        val url: String
    )
}