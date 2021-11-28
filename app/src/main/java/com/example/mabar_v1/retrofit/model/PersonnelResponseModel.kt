package com.example.mabar_v1.retrofit.model


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class PersonnelResponseModel(
    @SerializedName("code")
    @Expose
    var code: String,
    @SerializedName("data")
    @Expose
    var `data`: Data,
    @SerializedName("desc")
    @Expose
    var desc: String
) {
    data class Data(
        @SerializedName("address")
        @Expose
        var address: String,
        @SerializedName("birthdate")
        @Expose
        var birthdate: String,
        @SerializedName("district_id")
        @Expose
        var districtId: Any,
        @SerializedName("firstname")
        @Expose
        var firstname: String,
        @SerializedName("gender")
        @Expose
        var gender: String,
        @SerializedName("gender_id")
        @Expose
        var genderId: String,
        @SerializedName("lastname")
        @Expose
        var lastname: String,
        @SerializedName("phone")
        @Expose
        var phone: String,
        @SerializedName("province_id")
        @Expose
        var provinceId: String,
        @SerializedName("role")
        @Expose
        var role: Int,
        @SerializedName("sub_district_id")
        @Expose
        var subDistrictId: String,
        @SerializedName("team_id")
        @Expose
        var teamId: String,
        @SerializedName("user_id")
        @Expose
        var userId: Int,
        @SerializedName("zipcode")
        @Expose
        var zipcode: String
    )
}