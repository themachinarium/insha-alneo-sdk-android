package com.machinarum.alneo_sdk.ui.method.sms

import androidx.lifecycle.ViewModel
import com.machinarum.alneo_sdk.data.api.repository.AlneoRepo
import com.machinarum.alneo_sdk.utils.toFormattedString
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PaymentSMSVM(private val repository: AlneoRepo, argsPrice: Long) : ViewModel() {

    private val _formattedPrice = MutableStateFlow(argsPrice.toFormattedString())
    val formattedPrice = _formattedPrice.asStateFlow()
}