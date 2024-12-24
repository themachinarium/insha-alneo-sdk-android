package com.machinarum.alneo_sdk.data.api.services

import com.machinarum.alneo_sdk.data.models.request.CheckPaymentSessionRequest
import com.machinarum.alneo_sdk.data.models.request.CompleteContactlessRequest
import com.machinarum.alneo_sdk.data.models.request.CreateContactlessPaymentSessionRequest
import com.machinarum.alneo_sdk.data.models.request.CreatePaymentSessionRequest
import com.machinarum.alneo_sdk.data.models.request.RejectPaymentSessionRequest
import com.machinarum.alneo_sdk.data.models.response.BaseResponse
import com.machinarum.alneo_sdk.data.models.response.CreateContactlessResponse
import com.machinarum.alneo_sdk.data.models.response.CreatePaymentResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AlneoApiServices {

    @POST("create/session/contactless")
    suspend fun createContactlessPaymentSession(@Body request: CreateContactlessPaymentSessionRequest): BaseResponse<CreateContactlessResponse>

    @POST("complete/session/contactless")
    suspend fun completeContactlessPaymentSession(@Body request: CompleteContactlessRequest): BaseResponse<CreateContactlessResponse>

    @POST("session/create")
    suspend fun createPaymentSession(@Body request: CreatePaymentSessionRequest): BaseResponse<CreatePaymentResponse>

    @POST("session/check")
    suspend fun checkPaymentSession(@Body request: CheckPaymentSessionRequest): BaseResponse<String>

    @POST("session/finalize")
    suspend fun finalizePaymentSession(@Body request: CheckPaymentSessionRequest): BaseResponse<String>

    @POST("session/reject")
    suspend fun rejectPaymentSession(@Body request: RejectPaymentSessionRequest): BaseResponse<String>

}
