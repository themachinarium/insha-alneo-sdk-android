package com.machinarum.alneo_sdk.data.models.request

import com.squareup.moshi.Json

data class RejectPaymentSessionRequest(
    @Json(name = "payment_type")
    val payment_type: String? = null,
    @Json(name = "session_token")
    val session_token: String? = null
)
