package com.machinarum.alneo_sdk.ui.method.contactless

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.machinarum.alneo_sdk.data.api.repository.AlneoRepo
import com.machinarum.alneo_sdk.data.api.utils.onError
import com.machinarum.alneo_sdk.data.api.utils.onSuccess
import com.machinarum.alneo_sdk.data.models.request.CreateContactlessPaymentSessionRequest
import com.machinarum.alneo_sdk.utils.toFormattedString
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PaymentContactlessVM(val repository: AlneoRepo, val argsPrice: Long) : ViewModel() {

    private val _sessionToken = MutableStateFlow<String>("")
    val sessionToken = _sessionToken.asStateFlow()

    private val _formattedPrice = MutableStateFlow(argsPrice.toFormattedString())
    val formattedPrice = _formattedPrice.asStateFlow()

    private val _netPriceFormatted = MutableStateFlow("")
    val netPriceFormatted = _netPriceFormatted.asStateFlow()

    private val _commissionRateFormatted = MutableStateFlow("")
    val commissionRateFormatted = _commissionRateFormatted.asStateFlow()

    private val _errorMessage = MutableSharedFlow<String?>()
    val errorMessage = _errorMessage.asSharedFlow()

    private val _loading = MutableSharedFlow<Boolean>()
    val loading = _loading.asSharedFlow()


    init {
        createContactlessPaymentSession()
    }

    private fun createContactlessPaymentSession() = viewModelScope.launch {
        _loading.emit(true)
        val request = CreateContactlessPaymentSessionRequest(
            paymentChannel = "MOBILE_ANDROID",
            price = argsPrice / 100.0,
            currency = "TRY"
        )

        repository.createContactlessPaymentSession(request = request).onSuccess {
            _sessionToken.value = it.sessionToken.orEmpty()
            _loading.emit(false)
        }.onError { message, statusEnum, statusCode ->
            _loading.emit(false)
            _errorMessage.emit(message)
        }
    }
}