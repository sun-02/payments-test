package com.example.payments_test.data

import okhttp3.Interceptor
import okhttp3.Response

class HeadersInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        return chain.proceed(
            request.newBuilder()
                .header(APP_KEY, APP_KEY_VALUE)
                .header(VERSION, VERSION_VALUE)
                .build()
        )
    }
    companion object {
        private const val APP_KEY = "app-key"
        private const val APP_KEY_VALUE = "12345"
        private const val VERSION = "v"
        private const val VERSION_VALUE = "1"
    }
}