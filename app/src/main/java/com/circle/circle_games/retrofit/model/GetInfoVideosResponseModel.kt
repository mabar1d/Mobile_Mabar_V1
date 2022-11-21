package com.circle.circle_games.retrofit.model


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class GetInfoVideosResponseModel(
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
        val deletedAt: Any?,
        @SerializedName("diffCreatedAt")
        @Expose
        val diffCreatedAt: String?,
        @SerializedName("firstname")
        @Expose
        val firstname: String?,
        @SerializedName("lastname")
        @Expose
        val lastname: Any?,
        @SerializedName("link")
        @Expose
        val link: String?,
        @SerializedName("linkShare")
        @Expose
        val linkShare: String?,
        @SerializedName("notify")
        @Expose
        val notify: Any?,
        @SerializedName("slug")
        @Expose
        val slug: String?,
        @SerializedName("status")
        @Expose
        val status: String?,
        @SerializedName("tag")
        @Expose
        val tag: List<String?>?,
        @SerializedName("title")
        @Expose
        val title: String?,
        @SerializedName("updated_at")
        @Expose
        val updatedAt: String?,
        @SerializedName("updated_by")
        @Expose
        val updatedBy: String?,
        @SerializedName("video_id")
        @Expose
        val videoId: Int?
    )
}