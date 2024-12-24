package com.machinarum.alneo_sdk.data.models.response

import com.squareup.moshi.Json

data class CreateContactlessResponse(
    @Json(name = "session_token")
    val session_token: String?,
    @Json(name = "order_id")
    val order_id: String?,
    @Json(name = "commission_rate")
    val commission_rate: Double?
)

