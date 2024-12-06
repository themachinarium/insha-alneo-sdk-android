package com.machinarum.alneo_sdk.ui.method.qr

import androidx.lifecycle.ViewModel
import com.machinarum.alneo_sdk.data.api.repository.AlneoRepo
import com.machinarum.alneo_sdk.utils.OtpTimerUtil
import com.machinarum.alneo_sdk.utils.toFormattedString
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PaymentQRVM(private val repository: AlneoRepo, argsPrice: Long) : ViewModel() {
    private val _formattedPrice = MutableStateFlow(argsPrice.toFormattedString())
    val formattedPrice = _formattedPrice.asStateFlow()

    val timer = OtpTimerUtil()
    var formattedTime = timer.formattedTime

    init {
        timer.start(40000)
    }

}