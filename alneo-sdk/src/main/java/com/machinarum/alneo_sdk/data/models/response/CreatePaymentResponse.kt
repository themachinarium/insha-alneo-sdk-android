package com.machinarum.alneo_sdk.data.models.response

import com.squareup.moshi.Json

data class CreatePaymentResponse(
    @Json(name = "session_token")
    val session_token: String?,
    val timestamp: Long?
)


