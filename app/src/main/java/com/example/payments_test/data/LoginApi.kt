package com.example.payments_test.data

import com.example.payments_test.data.dto.LoginRequest
import com.example.payments_test.data.dto.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {

    @POST("/api-test/login")
    suspend fun login(
        @Body request: LoginRequest
    ) : LoginResponse
}