package com.example.mabar_v1.retrofit.model


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class GetInfoNewsResponseModel(
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
        @SerializedName("content")
        @Expose
        val content: String?,
        @SerializedName("created_at")
        @Expose
        val createdAt: String?,
        @SerializedName("created_by")
        @Expose
        val createdBy: String?,
        @SerializedName("deleted_at")
        @Expose
        val deletedAt: String?,
        @SerializedName("firstname")
        @Expose
        val firstname: String?,
        @SerializedName("lastname")
        @Expose
        val lastname: String?,
        @SerializedName("linkShare")
        @Expose
        val linkShare: String?,
        @SerializedName("diffCreatedAt")
        @Expose
        val diffCreatedAt: String?,
        @SerializedName("id")
        @Expose
        val id: Int?,
        @SerializedName("image")
        @Expose
        val image: String?,
        @SerializedName("news_category_id")
        @Expose
        val newsCategoryId: String?,
        @SerializedName("slug")
        @Expose
        val slug: String?,
        @SerializedName("status")
        @Expose
        val status: String?,
        @SerializedName("title")
        @Expose
        val title: String?,
        @SerializedName("updated_at")
        @Expose
        val updatedAt: String?,
        @SerializedName("updated_by")
        @Expose
        val updatedBy: String?
    )
}