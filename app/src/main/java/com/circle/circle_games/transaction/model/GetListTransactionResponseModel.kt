package com.circle.circle_games.transaction.model


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class GetListTransactionResponseModel(
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
        @SerializedName("expiry_time")
        @Expose
        val expiryTime: String? = "",
        @SerializedName("gross_amount")
        @Expose
        val grossAmount: String? = "",
        @SerializedName("id")
        @Expose
        val id: Int? = 0,
        @SerializedName("order_id")
        @Expose
        val orderId: String? = "",
        @SerializedName("item_name")
        @Expose
        val itemName: String? = "",
        @SerializedName("settlement_time")
        @Expose
        val settlementTime: String? = "",
        @SerializedName("status_code")
        @Expose
        val statusCode: String? = "",
        @SerializedName("transaction_status")
        @Expose
        val transactionStatus: String? = "",
        @SerializedName("transaction_time")
        @Expose
        val transactionTime: String? = "",
        @SerializedName("user_id")
        @Expose
        val userId: String? = "",
        @SerializedName("payment_type_name")
        @Expose
        val paymentTypeName: String? = "",
        @SerializedName("va_final")
        @Expose
        val vaFinal: String? = "",
        @SerializedName("va_number")
        @Expose
        val vaNumber: String? = "",
        @SerializedName("bank_name")
        @Expose
        val bankName: String? = ""
    )
}