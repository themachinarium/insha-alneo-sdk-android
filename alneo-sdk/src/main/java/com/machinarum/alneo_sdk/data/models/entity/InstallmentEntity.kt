package com.machinarum.alneo_sdk.data.models.entity

import androidx.databinding.ObservableBoolean

data class InstallmentEntity(
    val id: Int,
    val title: String,
    val description: String,
    val price: String,
    val selected: ObservableBoolean = ObservableBoolean(false)

)
