package com.circle.circle_games.retrofit.model


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class SuccessResponseDefaultModel(
    @SerializedName("code")
    @Expose
    var code: String,
    @SerializedName("desc")
    @Expose
    var desc: String
)