package com.machinarum.alneo_sdk.ui.method.contactless

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.machinarum.alneo_sdk.data.api.repository.AlneoRepo
import com.machinarum.alneo_sdk.data.api.utils.onError
import com.machinarum.alneo_sdk.data.api.utils.onSuccess
import com.machinarum.alneo_sdk.data.models.enums.Currency
import com.machinarum.alneo_sdk.data.models.enums.PaymentChannel
import com.machinarum.alneo_sdk.data.models.request.CompleteContactlessRequest
import com.machinarum.alneo_sdk.data.models.request.CreateContactlessPaymentSessionRequest
import com.machinarum.alneo_sdk.utils.Helper
import com.machinarum.alneo_sdk.utils.toFormattedString
import com.machinarum.alneo_sdk.utils.toPrice
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import java.util.Locale

class PaymentContactlessVM(private val repository: AlneoRepo, val argsPrice: Long) : ViewModel() {

    private val _sessionToken = MutableStateFlow("")
    val sessionToken = _sessionToken.asStateFlow()

    private val _formattedPrice = MutableStateFlow(argsPrice.toFormattedString())
    val formattedPrice = _formattedPrice.asStateFlow()

    private val _netPriceFormatted = MutableStateFlow("")
    val netPriceFormatted = _netPriceFormatted.asStateFlow()

    private val _commissionRateFormatted = MutableStateFlow("")
    val commissionRateFormatted = _commissionRateFormatted.asStateFlow()

    private val _paymentSession = MutableStateFlow("")
    val paymentSession = _paymentSession.asStateFlow()

    private val _orderId = MutableStateFlow("")
    val orderId = _orderId.asStateFlow()

    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage = _errorMessage.asSharedFlow()

    private val _loading = MutableSharedFlow<Boolean>()
    val loading = _loading.asSharedFlow()


    init {
        createContactlessPaymentSession()
    }

    private fun createContactlessPaymentSession() = viewModelScope.launch {
        _loading.emit(true)
        val request = CreateContactlessPaymentSessionRequest(
            PaymentChannel.MOBILE_ANDROID.name,
            price = argsPrice.toPrice(),
            currency = Currency.TRY.name
        )

        repository.createContactlessPaymentSession(request = request).onSuccess {
            _loading.emit(false)
            _sessionToken.value = it.session_token.orEmpty()
            _orderId.value = it.order_id.orEmpty()
            _paymentSession.value = "${it.session_token?.take(8)}**"
            _commissionRateFormatted.emit(
                String.format(
                    Locale.getDefault(),
                    "%% %.2f",
                    it.commission_rate
                )
            )
            val netPrice = Helper.calculateNetPrice(
                price = argsPrice.toPrice(),
                commissionRate = it.commission_rate ?: 0.0
            )
            _netPriceFormatted.emit(netPrice.toFormattedString())

        }.onError { message, _, _ ->
            _loading.emit(false)
            _errorMessage.emit(message.orEmpty())
        }
    }

    fun completeContactlessPaymentSession(nfcData: String) = viewModelScope.launch {
        _loading.emit(true)
        val request = CompleteContactlessRequest(
            session_token = sessionToken.value,
            price = argsPrice.toPrice(),
            currency = Currency.TRY.name,
            data = nfcData,
            channel = PaymentChannel.MOBILE_ANDROID.name,
            order_id = orderId.value
        )
        repository.completeContactlessPaymentSession(request = request).onSuccess {
            _loading.emit(false)

        }.onError { message, _, _ ->
            _loading.emit(false)
            _errorMessage.emit(message.orEmpty())
        }
    }
}