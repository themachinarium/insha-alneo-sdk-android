package com.machinarum.alneo_sdk.ui.method.qr.process

import androidx.lifecycle.ViewModel
import com.machinarum.alneo_sdk.data.api.repository.AlneoRepo
import com.machinarum.alneo_sdk.utils.toFormattedString
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PaymentQRProcessVM(
    private val repository: AlneoRepo,
    argsPrice: Long
) : ViewModel() {

    private val _formattedPrice = MutableStateFlow(argsPrice.toFormattedString())
    val formattedPrice = _formattedPrice.asStateFlow()

    private val _receiptId= MutableStateFlow("1222313")
    val receiptId = _receiptId.asStateFlow()

}