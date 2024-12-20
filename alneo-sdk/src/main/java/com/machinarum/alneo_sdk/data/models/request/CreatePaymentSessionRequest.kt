package com.machinarum.alneo_sdk.data.models.request

import com.squareup.moshi.Json


data class CreatePaymentSessionRequest(
    @Json(name = "payment_channel")
    val payment_channel: String? = null,

    @Json(name = "payment_type")
    val payment_type: String? = null,

    @Json(name = "price")
    val price: Double? = null,

    @Json(name = "currency")
    val currency: String? = null,

    @Json(name = "description")
    val description: String? = null,

    @Json(name = "source")
    val source: String? = null,

    @Json(name = "data")
    val data: String? = null

)
