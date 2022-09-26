package com.example.mabar_v1.retrofit.model


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class GetInfoNewsCategoryResponseModel(
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
        val createdAt: String?,
        @SerializedName("created_by")
        @Expose
        val createdBy: String?,
        @SerializedName("deleted_at")
        @Expose
        val deletedAt: Any?,
        @SerializedName("desc")
        @Expose
        val desc: String?,
        @SerializedName("id")
        @Expose
        val id: Int?,
        @SerializedName("name")
        @Expose
        val name: String?,
        @SerializedName("status")
        @Expose
        val status: String?,
        @SerializedName("updated_at")
        @Expose
        val updatedAt: String?,
        @SerializedName("updated_by")
        @Expose
        val updatedBy: Any?
    )
}