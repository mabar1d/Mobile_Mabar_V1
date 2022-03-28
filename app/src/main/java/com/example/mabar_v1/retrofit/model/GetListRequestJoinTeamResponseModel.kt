package com.example.mabar_v1.retrofit.model


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class GetListRequestJoinTeamResponseModel(
    @SerializedName("code")
    @Expose
    var code: String,
    @SerializedName("data")
    @Expose
    var `data`: List<Data>,
    @SerializedName("desc")
    @Expose
    var desc: String
) {
    data class Data(
        @SerializedName("user_request_id")
        @Expose
        var userRequestId: Int,
        @SerializedName("user_request_name")
        @Expose
        var userRequestName: String
    )
}