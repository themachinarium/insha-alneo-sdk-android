package com.machinarum.alneo_sdk.ui.payment_method

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.machinarum.alneo_sdk.data.api.repository.AlneoRepo
import com.machinarum.alneo_sdk.data.api.utils.onError
import com.machinarum.alneo_sdk.data.api.utils.onSuccess
import com.machinarum.alneo_sdk.data.models.request.CreateContactlessPaymentSessionRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PaymentMethodVM(val repository: AlneoRepo) : ViewModel() {

    private val _sessionToken = MutableStateFlow<String>("")
    val sessionToken = _sessionToken.asStateFlow()

    init {
        createContactlessPaymentSession()
    }

    private fun createContactlessPaymentSession() = viewModelScope.launch {
        val request = CreateContactlessPaymentSessionRequest(
            paymentChannel = "MOBILE_ANDROID",
            price = 100.0,
            currency = "TRY"
        )

        repository.createContactlessPaymentSession(request = request).onSuccess {
            _sessionToken.value = it.sessionToken.orEmpty()
        }.onError { message, statusEnum, statusCode ->
            Log.e("createContactlessPaymentSession", "createContactlessPaymentSession: $message")
        }
    }
}