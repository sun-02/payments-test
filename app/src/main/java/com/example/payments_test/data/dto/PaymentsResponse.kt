package com.example.payments_test.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PaymentsResponse(
    @Json(name = "success") val success: String,
    @Json(name = "response") val paymentDto: List<PaymentDto>? = null,
    @Json(name = "error") val error: ResponseError? = null,
)