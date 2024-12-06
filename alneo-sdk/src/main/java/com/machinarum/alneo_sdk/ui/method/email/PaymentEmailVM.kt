package com.machinarum.alneo_sdk.ui.method.email

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.machinarum.alneo_sdk.R
import com.machinarum.alneo_sdk.data.api.repository.AlneoRepo
import com.machinarum.alneo_sdk.utils.Helper
import com.machinarum.alneo_sdk.utils.toFormattedString
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class PaymentEmailVM(private val repository: AlneoRepo, argsPrice: Long) : ViewModel() {

    private val _formattedPrice = MutableStateFlow(argsPrice.toFormattedString())
    val formattedPrice = _formattedPrice.asStateFlow()

    val email = MutableStateFlow("")
    val description = MutableStateFlow("")

    private val _emailError = MutableSharedFlow<String?>()
    val emailError = _emailError.asSharedFlow()

    private val _navigateToPaymentProcess = MutableSharedFlow<Boolean>()
    val navigateToPaymentProcess = _navigateToPaymentProcess.asSharedFlow()

    fun validateEmail(context: Context) = viewModelScope.launch {
        when {
            email.value.isEmpty() -> {
                _emailError.emit(value = context.resources.getString(R.string.message_email_can_not_be_empty))
            }

            !Helper.validateEmail(email = email.value) -> {
                _emailError.emit(value = context.resources.getString(R.string.message_email_not_valid))
            }
            else -> {
                _navigateToPaymentProcess.emit(true)
            }


        }

    }
}