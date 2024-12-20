package com.machinarum.alneo_sdk.data.api.repository

import com.machinarum.alneo_sdk.data.api.services.AlneoApiServices
import com.machinarum.alneo_sdk.data.api.utils.ResponseHandler
import com.machinarum.alneo_sdk.data.models.request.CheckPaymentSessionRequest
import com.machinarum.alneo_sdk.data.models.request.CompleteContactlessRequest
import com.machinarum.alneo_sdk.data.models.request.CreateContactlessPaymentSessionRequest
import com.machinarum.alneo_sdk.data.models.request.CreatePaymentSessionRequest
import com.machinarum.alneo_sdk.data.models.request.RejectPaymentSessionRequest

class AlneoRepo(private val api: AlneoApiServices) : BaseRepo() {


    suspend fun createContactlessPaymentSession(request: CreateContactlessPaymentSessionRequest) =
        callApi {
            val response = api.createContactlessPaymentSession(request)
            ResponseHandler.handleResponse(response)
        }

    suspend fun completeContactlessPaymentSession(request: CompleteContactlessRequest) = callApi {
        val response = api.completeContactlessPaymentSession(request = request)
        ResponseHandler.handleResponse(response)

    }

    suspend fun createPaymentSession(request: CreatePaymentSessionRequest) =
        callApi {
            val response = api.createPaymentSession(request)
            ResponseHandler.handleResponse(response)
        }

    suspend fun checkPaymentSession(request: CheckPaymentSessionRequest) =
        callApi {
            val response = api.checkPaymentSession(request)
            ResponseHandler.handleResponse(response)
        }

    suspend fun finalizePaymentSession(request: CheckPaymentSessionRequest) =
        callApi {
            val response = api.finalizePaymentSession(request)
            ResponseHandler.handleResponse(response)
        }

    suspend fun rejectPaymentSession(request: RejectPaymentSessionRequest) =
        callApi {
            val response = api.rejectPaymentSession(request)
            ResponseHandler.handleResponse(response)
        }


}