package com.machinarum.alneo_sdk.data.api.interceptors

import okhttp3.logging.HttpLoggingInterceptor

class HttpLoggingInterceptor {
    companion object {
        fun getInterceptor(): HttpLoggingInterceptor {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            return interceptor
        }
    }
}