package com.example.payments_test.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResponseError(
    @Json(name = "error_code") val errorCode: Int = 0,
    @Json(name = "error_msg") val errorMsg: String = ""
)