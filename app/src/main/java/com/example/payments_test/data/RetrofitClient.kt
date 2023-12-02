package com.example.movies_app.data.core.remote

import com.example.movies_app.BuildConfig
import com.example.payments_test.data.AuthInterceptor
import com.example.payments_test.data.HeadersInterceptor
import com.example.payments_test.data.LoginApi
import com.example.payments_test.data.PaymentsApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://easypay.world"

    private val client = if (BuildConfig.DEBUG) {
        OkHttpClient.Builder()
            .addInterceptor(HeadersInterceptor())
            .addInterceptor(AuthInterceptor())
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    } else {
        OkHttpClient.Builder()
            .addInterceptor(HeadersInterceptor())
            .addInterceptor(AuthInterceptor())
            .build()
    }

    private val retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    val loginApi: LoginApi by lazy { retrofit.create(LoginApi::class.java) }

    val paymentsApi: PaymentsApi by lazy { retrofit.create(PaymentsApi::class.java) }
}