package com.circle.circle_games.retrofit.model


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class ListPaymentResponseModel(
    @SerializedName("code")
    @Expose
    val code: String?,
    @SerializedName("data")
    @Expose
    val `data`: List<Data?>?,
    @SerializedName("desc")
    @Expose
    val desc: String?
) {
    data class Data(
        @SerializedName("created_at")
        @Expose
        val createdAt: String?,
        @SerializedName("id")
        @Expose
        val id: Int?,
        @SerializedName("id_payment")
        @Expose
        val idPayment: String?,
        @SerializedName("id_user")
        @Expose
        val idUser: Int?,
        @SerializedName("payment")
        @Expose
        val payment: String?,
        @SerializedName("updated_at")
        @Expose
        val updatedAt: String?
    )
}