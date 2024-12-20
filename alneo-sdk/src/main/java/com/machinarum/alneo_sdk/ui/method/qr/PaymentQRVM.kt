package com.machinarum.alneo_sdk.ui.method.qr

import androidx.compose.ui.util.trace
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.machinarum.alneo_sdk.data.api.repository.AlneoRepo
import com.machinarum.alneo_sdk.data.api.utils.onError
import com.machinarum.alneo_sdk.data.api.utils.onSuccess
import com.machinarum.alneo_sdk.data.models.enums.PaymentChannel
import com.machinarum.alneo_sdk.data.models.enums.PaymentType
import com.machinarum.alneo_sdk.data.models.enums.SessionProgress
import com.machinarum.alneo_sdk.data.models.request.CheckPaymentSessionRequest
import com.machinarum.alneo_sdk.data.models.request.CreatePaymentSessionRequest
import com.machinarum.alneo_sdk.utils.OtpTimerUtil
import com.machinarum.alneo_sdk.utils.toFormattedString
import com.machinarum.alneo_sdk.utils.toPrice
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date

class PaymentQRVM(private val repository: AlneoRepo, val argsPrice: Long) : ViewModel() {
    private val _formattedPrice = MutableStateFlow(argsPrice.toFormattedString())
    val formattedPrice = _formattedPrice.asStateFlow()

    private val _sessionToken = MutableStateFlow("")
    val sessionToken = _sessionToken.asStateFlow()

    private val _errorMessages = MutableStateFlow("")
    val errorMessages = _errorMessages.asStateFlow()

    val timer = OtpTimerUtil()
    var formattedTime = timer.formattedTime

    private val _navigateToProgress = MutableSharedFlow<Boolean>()
    val navigateToProgress = _navigateToProgress.asSharedFlow()

    private val _sessionProgress = MutableStateFlow(SessionProgress.NONE)
    val sessionProgress = _sessionProgress.asStateFlow()

    fun createPaymentSession() = viewModelScope.launch {
        repository.createPaymentSession(
            request = CreatePaymentSessionRequest(
                payment_channel = PaymentChannel.MOBILE_ANDROID.name,
                payment_type = PaymentType.QR.name,
                price = argsPrice.toPrice(),
            )
        ).onSuccess { response ->
            val timestamp = response.timestamp ?: 0L
            var currentTimestamp = Date().time
            if (currentTimestamp.toString().length > 10) {
                currentTimestamp = (currentTimestamp / 1000L)
            }

            val lifetime: Long = (300000 - (currentTimestamp - timestamp))
            timer.start(lifetime.toInt())
            _sessionToken.value = response.session_token.orEmpty()
            checkPaymentSession()

        }.onError { message, _, _ ->
            _errorMessages.value = message.orEmpty()
        }
    }

    init {
        createPaymentSession()
    }

    private fun checkPaymentSession(): Job = viewModelScope.launch {
        repository.checkPaymentSession(
            request = CheckPaymentSessionRequest(
                payment_type = PaymentType.QR.name,
                session_token = sessionToken.value,
            )
        ).onSuccess {
            when (val progressEnum = SessionProgress.valueOf(it)) {
                SessionProgress.INITIAL -> {
                    delay(5000)
                    checkPaymentSession()
                }

                SessionProgress.HANSHAKED -> {
                    timer.stop()
                    _navigateToProgress.emit(true)
                }

                else -> {
                    _sessionProgress.value = progressEnum
                }
            }

        }.onError { message, _, _ ->
            _errorMessages.value = message.orEmpty()
        }
    }

    fun finalizePaymentSession() = viewModelScope.launch {
        if (sessionToken.value.isEmpty()) return@launch
        repository.finalizePaymentSession(
            CheckPaymentSessionRequest(
                session_token = sessionToken.value,
                payment_type = null
            )
        ).onSuccess {

        }
    }
}