package com.machinarum.alneo_sdk.data.api.services

import com.machinarum.alneo_sdk.data.models.request.CreateContactlessPaymentSessionRequest
import com.machinarum.alneo_sdk.data.models.request.CreatePaymentSessionRequest
import com.machinarum.alneo_sdk.data.models.response.CreateContactlessPaymentSessionResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AlneoApiServices {

    @POST("/service/payment/create/session/contactless")
    suspend fun createContactlessPaymentSession(@Body request: CreateContactlessPaymentSessionRequest): CreateContactlessPaymentSessionResponse


    @POST("/service/payment/create/session")
    suspend fun createPaymentSession(@Body request: CreatePaymentSessionRequest): CreateContactlessPaymentSessionResponse

}
