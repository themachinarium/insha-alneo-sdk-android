package com.machinarum.alneo_sdk.ui.method.sms.process

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.machinarum.alneo_sdk.data.api.repository.AlneoRepo
import com.machinarum.alneo_sdk.data.api.utils.onError
import com.machinarum.alneo_sdk.data.api.utils.onSuccess
import com.machinarum.alneo_sdk.data.models.enums.Currency
import com.machinarum.alneo_sdk.data.models.enums.PaymentChannel
import com.machinarum.alneo_sdk.data.models.enums.PaymentStatus
import com.machinarum.alneo_sdk.data.models.enums.PaymentType
import com.machinarum.alneo_sdk.data.models.request.CreatePaymentSessionRequest
import com.machinarum.alneo_sdk.utils.toFormattedString
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PaymentProcessVM(
    private val repository: AlneoRepo,
    val argsPrice: Long
) : ViewModel() {

    private val _formattedPrice = MutableStateFlow(argsPrice.toFormattedString())
    val formattedPrice = _formattedPrice.asStateFlow()

    private val _paymentStatus = MutableStateFlow(PaymentStatus.DEFAULT)
    val paymentStatus = _paymentStatus.asStateFlow()

    fun createPaymentSession(description: String,paymentType: PaymentType,data: String?) = viewModelScope.launch {

       _paymentStatus.value = PaymentStatus.LOADING
        val request = CreatePaymentSessionRequest(
            price = argsPrice / 100.0,
            currency = Currency.TRY.name,
            payment_channel = PaymentChannel.WEB.name,
            source = "ANDROID",
            description = description,
            payment_type = paymentType.name,
            data = data

        )
        repository.createPaymentSession(
            request
        ).onSuccess {
            _paymentStatus.value = PaymentStatus.SUCCESS

        }.onError { _, _, _ ->
            _paymentStatus.value = PaymentStatus.ERROR

        }

    }

}