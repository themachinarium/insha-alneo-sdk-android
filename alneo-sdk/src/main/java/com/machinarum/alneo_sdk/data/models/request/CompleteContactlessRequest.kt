package com.machinarum.alneo_sdk.data.models.request

import com.machinarum.alneo_sdk.data.models.enums.PaymentChannel
import com.squareup.moshi.Json


data class CompleteContactlessRequest(
    @Json(name = "data")
    val data: String,

    @Json(name = "price")
    val price: Double,

    @Json(name = "currency")
    val currency: String,

    @Json(name = "session_token")
    val session_token: String,

    @Json(name = "order_id")
    val order_id: String,

    @Json(name = "channel")
    val channel: String = PaymentChannel.MOBILE_ANDROID.name
)

