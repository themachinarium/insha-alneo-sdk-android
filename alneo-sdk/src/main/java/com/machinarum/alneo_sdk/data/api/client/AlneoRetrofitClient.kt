package com.machinarum.alneo_sdk.data.api.client

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.machinarum.alneo_sdk.data.api.interceptors.HttpLoggingInterceptor
import com.machinarum.alneo_sdk.data.api.services.AlneoApiServices
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

object AlneoRetrofitClient {

    private val BASE_URL = "https://api.alneopos.com"
    fun provideAlneoApi(): AlneoApiServices {
        return getRetrofit().create(AlneoApiServices::class.java)
    }

    private fun getRetrofit(): Retrofit {
        val client =
            OkHttpClient.Builder().readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor { chain ->
                    val original = chain.request()

                    val requestBuilder = original.newBuilder()
                        .method(original.method, original.body)
                        .addHeader("Content-Type", "application/json")
                    val request = requestBuilder.build()
                    chain.proceed(request)

                }

                .addNetworkInterceptor(HttpLoggingInterceptor.getInterceptor()).build()

        return Retrofit.Builder().baseUrl(BASE_URL)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(client).build()
    }

}