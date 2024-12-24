package com.machinarum.alneo_sdk.data.models.response

import com.squareup.moshi.Json

data class BaseResponse<T>(
    @Json(name = "code")
    val code: Int?,
    @Json(name = "message")
    val message: String?,
    @Json(name = "content")
    val content: T?
)
