package com.machinarum.alneo_sdk.data.api.services

import com.google.gson.JsonArray
import com.machinarum.alneo_sdk.data.models.request.LoginRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface AlneoApiServices {

    @POST("/service/auth/login")
    suspend fun login(@Body request: LoginRequest): JsonArray

}
