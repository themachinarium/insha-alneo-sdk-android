package com.machinarum.alneo_sdk.data.models.request

import com.squareup.moshi.Json

data class CreatePaymentSessionRequest(
    @Json(name = "payment_channel")
    val paymentChannel: String,

    @Json(name = "payment_type")
    val paymentType: String,

    @Json(name = "price")
    val price: Double,

    @Json(name = "currency")
    val currency: String,

    @Json(name = "description")
    val description: String,

    @Json(name = "source")
    val source: String

)
