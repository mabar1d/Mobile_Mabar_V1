package com.circle.circle_games.login.model


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class LoginResponseModel(
    @SerializedName("code")
    @Expose
    val code: String? = "",
    @SerializedName("data")
    @Expose
    val `data`: Data? = Data(),
    @SerializedName("desc")
    @Expose
    val desc: String? = ""
) {
    data class Data(
        @SerializedName("access_token")
        @Expose
        val accessToken: String? = "",
        @SerializedName("detail_personnel")
        @Expose
        val detailPersonnel: DetailPersonnel? = DetailPersonnel(),
        @SerializedName("expires_in")
        @Expose
        val expiresIn: String? = "",
        @SerializedName("token_type")
        @Expose
        val tokenType: String? = "",
        @SerializedName("user")
        @Expose
        val user: User? = User()
    ) {
        data class DetailPersonnel(
            @SerializedName("address")
            @Expose
            val address: String? = "",
            @SerializedName("birthdate")
            @Expose
            val birthdate: String? = "",
            @SerializedName("district_id")
            @Expose
            val districtId: String? = "",
            @SerializedName("email")
            @Expose
            val email: String? = "",
            @SerializedName("firstname")
            @Expose
            val firstname: String? = "",
            @SerializedName("gender")
            @Expose
            val gender: String? = "",
            @SerializedName("gender_id")
            @Expose
            val genderId: String? = "",
            @SerializedName("ign")
            @Expose
            val ign: String? = "",
            @SerializedName("image")
            @Expose
            val image: String? = "",
            @SerializedName("is_verified")
            @Expose
            val isVerified: String? = "",
            @SerializedName("lastname")
            @Expose
            val lastname: String? = "",
            @SerializedName("phone")
            @Expose
            val phone: String? = "",
            @SerializedName("province_id")
            @Expose
            val provinceId: String? = "",
            @SerializedName("role")
            @Expose
            val role: String? = "",
            @SerializedName("sub_district_id")
            @Expose
            val subDistrictId: String? = "",
            @SerializedName("team_id")
            @Expose
            val teamId: String? = "",
            @SerializedName("user_id")
            @Expose
            val userId: String? = "",
            @SerializedName("username")
            @Expose
            val username: String? = "",
            @SerializedName("zipcode")
            @Expose
            val zipcode: String? = ""
        )

        data class User(
            @SerializedName("created_at")
            @Expose
            val createdAt: String? = "",
            @SerializedName("email")
            @Expose
            val email: String? = "",
            @SerializedName("id")
            @Expose
            val id: Int? = 0,
            @SerializedName("token_firebase")
            @Expose
            val tokenFirebase: String? = "",
            @SerializedName("token_jwt")
            @Expose
            val tokenJwt: String? = "",
            @SerializedName("updated_at")
            @Expose
            val updatedAt: String? = "",
            @SerializedName("username")
            @Expose
            val username: String? = ""
        )
    }
}