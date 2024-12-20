package com.machinarum.alneo_sdk.data.models.request

import com.squareup.moshi.Json

data class CreateContactlessPaymentSessionRequest(

    @Json(name = "payment_channel")
    val payment_channel: String,
    @Json(name = "price")
    val price: Double,
    @Json(name = "currency")
    val currency: String

)
