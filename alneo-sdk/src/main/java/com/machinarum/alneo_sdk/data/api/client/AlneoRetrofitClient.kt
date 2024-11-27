package com.machinarum.alneo_sdk.data.api.client

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import com.machinarum.alneo_sdk.data.api.interceptors.HttpLoggingInterceptor
import com.machinarum.alneo_sdk.data.api.services.AlneoApiServices
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

object AlneoRetrofitClient {

    private const val BASE_URL = "https://alneomac.2sworks.com"


    fun provideAlneoApi(context: Context?): AlneoApiServices {
        val applicationInfo = context?.packageManager?.getApplicationInfo(
            context.packageName,
            PackageManager.GET_META_DATA
        )

        val bundle = applicationInfo?.metaData

        val header = bundle?.getString("ALNEO_SECURITY_HEADER")

        return getRetrofit(header).create(AlneoApiServices::class.java)
    }

    private fun getRetrofit(header: String?): Retrofit {
        val client = OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
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

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(client)
            .build()
    }
}