package com.machinarum.alneo_sdk.data.api.repository

import com.machinarum.alneo_sdk.data.api.services.AlneoApiServices
import com.machinarum.alneo_sdk.data.api.utils.ResponseHandler
import com.machinarum.alneo_sdk.data.models.request.LoginRequest

class AlneoRepo(private val api: AlneoApiServices) : BaseRepo() {


    suspend fun login(loginRequest: LoginRequest) = callApi {
        val response = api.login(request = loginRequest)
        ResponseHandler.handleSuccess(response)
    }


}