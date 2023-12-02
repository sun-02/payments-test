package com.example.payments_test.data

import com.example.payments_test.data.dto.PaymentsResponse
import retrofit2.http.GET

interface PaymentsApi {

    @GET("/api-test/payments")
    suspend fun fetchPayments(): PaymentsResponse
}