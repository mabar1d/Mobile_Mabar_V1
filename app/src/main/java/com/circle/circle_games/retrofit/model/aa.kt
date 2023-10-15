package com.circle.circle_games.retrofit.model


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class aa(
    @SerializedName("code")
    @Expose
    val code: String? = "",
    @SerializedName("data")
    @Expose
    val `data`: List<Data?>? = listOf(),
    @SerializedName("desc")
    @Expose
    val desc: String? = ""
) {
    data class Data(
        @SerializedName("category_id")
        @Expose
        val categoryId: Int? = 0,
        @SerializedName("category_name")
        @Expose
        val categoryName: String? = "",
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
        @SerializedName("link")
        @Expose
        val link: String? = "",
        @SerializedName("linkShare")
        @Expose
        val linkShare: String? = "",
        @SerializedName("notify")
        @Expose
        val notify: Any? = Any(),
        @SerializedName("slug")
        @Expose
        val slug: String? = "",
        @SerializedName("status")
        @Expose
        val status: Int? = 0,
        @SerializedName("tag")
        @Expose
        val tag: Any? = Any(),
        @SerializedName("title")
        @Expose
        val title: String? = "",
        @SerializedName("video_id")
        @Expose
        val videoId: Int? = 0
    )
}