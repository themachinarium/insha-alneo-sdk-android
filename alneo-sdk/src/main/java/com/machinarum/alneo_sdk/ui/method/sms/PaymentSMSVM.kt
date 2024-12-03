package com.machinarum.alneo_sdk.ui.method.sms

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.machinarum.alneo_sdk.R
import com.machinarum.alneo_sdk.data.api.repository.AlneoRepo
import com.machinarum.alneo_sdk.utils.toFormattedString
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PaymentSMSVM(private val repository: AlneoRepo, argsPrice: Long) : ViewModel() {

    private val _formattedPrice = MutableStateFlow(argsPrice.toFormattedString())
    val formattedPrice = _formattedPrice.asStateFlow()

    val phoneNumber = MutableStateFlow("")
    val description = MutableStateFlow("")

    private val _phoneNumberError = MutableSharedFlow<String?>()
    val phoneNumberError = _phoneNumberError.asSharedFlow()

    fun validatePhoneNumber(context: Context) = viewModelScope.launch {
        when {
            phoneNumber.value.isEmpty() -> {
                _phoneNumberError.emit(value = context.resources.getString(R.string.message_phone_number_can_not_be_empty))

            }

            phoneNumber.value.length < 11 -> {
                _phoneNumberError.emit(value = context.resources.getString(R.string.message_fit_phone_number_digits))
            }

            else -> {
                _phoneNumberError.emit(null)
            }
        }
    }
}