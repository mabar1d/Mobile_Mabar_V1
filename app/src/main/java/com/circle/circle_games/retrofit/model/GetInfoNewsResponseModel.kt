package com.circle.circle_games.retrofit.model


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
            val content: String? = "",
            @SerializedName("created_at")
            @Expose
            val createdAt: String? = "",
            @SerializedName("created_by")
            @Expose
            val createdBy: Int? = 0,
            @SerializedName("created_by_name")
            @Expose
            val createdByName: String? = "",
            @SerializedName("diffCreatedAt")
            @Expose
            val diffCreatedAt: String? = "",
            @SerializedName("id")
            @Expose
            val id: Int? = 0,
            @SerializedName("image")
            @Expose
            val image: String? = "",
            @SerializedName("link_share")
            @Expose
            val linkShare: String? = "",
            @SerializedName("news_category_id")
            @Expose
            val newsCategoryId: Int? = 0,
            @SerializedName("news_category_name")
            @Expose
            val newsCategoryName: String? = "",
            @SerializedName("news_image_url")
            @Expose
            val newsImageUrl: String? = "",
            @SerializedName("slug")
            @Expose
            val slug: String? = "",
            @SerializedName("status")
            @Expose
            val status: Int? = 0,
            @SerializedName("title")
            @Expose
            val title: String? = ""
    )
}