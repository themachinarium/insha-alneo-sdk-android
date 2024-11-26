package com.machinarum.alneo_sdk.data.models.request

import com.google.gson.annotations.SerializedName


data class LoginRequest(
    @SerializedName("phoneNumber")
    var phoneNumber: String,
    @SerializedName("code")
    var code: String
)