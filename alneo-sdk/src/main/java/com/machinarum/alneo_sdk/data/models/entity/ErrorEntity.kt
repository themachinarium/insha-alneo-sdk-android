package com.machinarum.alneo_sdk.data.models.entity

import com.google.gson.annotations.SerializedName

data class ErrorEntity(
    @SerializedName("status")
    val code: String?,
    @SerializedName("error")
    val message: String?
)
