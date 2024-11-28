package com.machinarum.alneo_sdk.data.api.repository

import com.machinarum.alneo_sdk.data.api.services.AlneoApiServices
import com.machinarum.alneo_sdk.data.api.utils.ResponseHandler
import com.machinarum.alneo_sdk.data.models.request.CreateContactlessPaymentSessionRequest

class AlneoRepo(private val api: AlneoApiServices) : BaseRepo() {


    suspend fun createContactlessPaymentSession(request: CreateContactlessPaymentSessionRequest) =
        callApi {
            val response = api.createContactlessPaymentSession(request)
            ResponseHandler.handleSuccess(response)
        }


}