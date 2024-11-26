package com.machinarum.alneo_sdk.data.models.entity

import com.google.gson.annotations.SerializedName

data class ErrorEntity(
    @SerializedName("code")
    val code: String?,
    @SerializedName("message")
    val message: String?
)
