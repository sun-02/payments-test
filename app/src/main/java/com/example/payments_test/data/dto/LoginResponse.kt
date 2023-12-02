package com.example.payments_test.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginResponse(
    @Json(name = "success") val success: String,
    @Json(name = "response") val token: Token? = null,
    @Json(name = "error") val error: ResponseError? = null,
)