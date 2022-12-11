package com.circle.circle_games.retrofit.model


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class CheckVersionResponseModel(
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
        @SerializedName("version_apk")
        @Expose
        val versionApk: String?,
        @SerializedName("version_database")
        @Expose
        val versionDatabase: List<VersionDatabase?>?
    ) {
        data class VersionDatabase(
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
}