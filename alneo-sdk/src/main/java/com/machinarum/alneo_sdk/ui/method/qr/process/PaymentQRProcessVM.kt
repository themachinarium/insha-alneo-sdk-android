package com.machinarum.alneo_sdk.ui.method.qr.process

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.machinarum.alneo_sdk.data.api.repository.AlneoRepo
import com.machinarum.alneo_sdk.data.api.utils.onError
import com.machinarum.alneo_sdk.data.api.utils.onSuccess
import com.machinarum.alneo_sdk.data.models.enums.PaymentType
import com.machinarum.alneo_sdk.data.models.enums.SessionProgress
import com.machinarum.alneo_sdk.data.models.request.CheckPaymentSessionRequest
import com.machinarum.alneo_sdk.data.models.request.RejectPaymentSessionRequest
import com.machinarum.alneo_sdk.utils.toFormattedString
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PaymentQRProcessVM(
    private val repository: AlneoRepo,
    argsPrice: Long,
    val sessionToken: String
) : ViewModel() {

    private val _formattedPrice = MutableStateFlow(argsPrice.toFormattedString())
    val formattedPrice = _formattedPrice.asStateFlow()

    private val _receiptId = MutableStateFlow("-")
    val receiptId = _receiptId.asStateFlow()

    private val _sessionProgress = MutableStateFlow(SessionProgress.NONE)
    val sessionProgress = _sessionProgress.asStateFlow()

    private val _errorMessages = MutableStateFlow("")
    val errorMessages = _errorMessages.asStateFlow()

    private val _navigateToBack = MutableSharedFlow<Boolean>()
    val navigateToBack = _navigateToBack.asSharedFlow()

    fun checkPaymentSession(): Job = viewModelScope.launch {
        repository.checkPaymentSession(
            request = CheckPaymentSessionRequest(
                payment_type = PaymentType.QR.name,
                session_token = sessionToken,
            )
        ).onSuccess {
            when (val progressEnum = SessionProgress.valueOf(it)) {

                SessionProgress.HANSHAKED, SessionProgress.PAYMENT_REQUEST_CREATED -> {
                    delay(5000)
                    checkPaymentSession()
                }

                else -> {
                    _sessionProgress.value = progressEnum
                }
            }

        }.onError { message, _, _ ->
            _errorMessages.value = message.orEmpty()
        }
    }

    fun rejectPaymentSession() = viewModelScope.launch {
        repository.rejectPaymentSession(
            RejectPaymentSessionRequest(
                payment_type = PaymentType.QR.name,
                session_token = sessionToken
            )
        ).onSuccess {
            _navigateToBack.emit(true)
        }.onError { message, statusEnum, statusCode ->
            _navigateToBack.emit(true)
        }
    }

}