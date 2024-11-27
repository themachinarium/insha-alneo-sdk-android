package com.machinarum.alneo_sdk.data.models.response

import com.squareup.moshi.Json

data class CreateContactlessPaymentSessionResponse(
    @Json(name = "session_token")
    val sessionToken: String?,
    @Json(name = "order_id")
    val orderId: String?,
    )
