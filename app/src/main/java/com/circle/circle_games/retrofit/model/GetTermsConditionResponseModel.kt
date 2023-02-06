package com.circle.circle_games.retrofit.model


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class GetTermsConditionResponseModel(
    @SerializedName("code")
    @Expose
    val code: String?,
    @SerializedName("data")
    @Expose
    val `data`: Data?,
    @SerializedName("desc")
    @Expose
    val desc: String?
) {
    data class Data(
        @SerializedName("created_at")
        @Expose
        val createdAt: Any?,
        @SerializedName("desc")
        @Expose
        val desc: String?,
        @SerializedName("id")
        @Expose
        val id: Int?,
        @SerializedName("status")
        @Expose
        val status: String?,
        @SerializedName("type")
        @Expose
        val type: String?,
        @SerializedName("updated_at")
        @Expose
        val updatedAt: String?
    )
}