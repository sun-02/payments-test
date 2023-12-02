package com.example.payments_test.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PaymentDto(
    @Json(name = "id") val id: Int = 0,
    @Json(name = "title") val title: String = "",
//    @Json(name = "amount") val amountD: Double? = null,
    @Json(name = "amount") val amount: String? = null,
    @Json(name = "created") val created: Int? = null,
)