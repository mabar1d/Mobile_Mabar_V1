package com.example.mabar_v1.retrofit.model


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class GetListMenuResponseModel(
    @SerializedName("code")
    @Expose
    val code: String,
    @SerializedName("data")
    @Expose
    val `data`: List<Data>,
    @SerializedName("desc")
    @Expose
    val desc: String
) {
    data class Data(
        @SerializedName("id")
        @Expose
        val id: Int,
        @SerializedName("order")
        @Expose
        val order: String,
        @SerializedName("title")
        @Expose
        val title: String
    )
}