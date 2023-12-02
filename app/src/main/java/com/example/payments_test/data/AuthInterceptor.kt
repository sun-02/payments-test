package com.example.payments_test.data

import com.example.payments_test.PaymentsApplication
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val token = PaymentsApplication.get().sessionManager.authToken

        return chain.proceed(
            if (token != null) {
                request.newBuilder()
                    .header(TOKEN, token)
                    .build()
            } else {
                request
            }
        )
    }

    companion object {
        private const val TOKEN = "Token"
    }
}