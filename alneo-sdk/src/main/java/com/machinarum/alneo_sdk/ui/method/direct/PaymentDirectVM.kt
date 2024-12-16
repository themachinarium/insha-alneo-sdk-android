package com.machinarum.alneo_sdk.ui.method.direct

import androidx.lifecycle.ViewModel
import com.machinarum.alneo_sdk.data.api.repository.AlneoRepo
import io.card.payment.CreditCard
import kotlinx.coroutines.flow.MutableStateFlow

class PaymentDirectVM(private val repository: AlneoRepo, argsPrice: Long) : ViewModel() {

    val cardholderName = MutableStateFlow("")
    val cardNumber = MutableStateFlow("")
    val expiryDate = MutableStateFlow("")
    val cvv = MutableStateFlow("")

    val phoneNumber = MutableStateFlow("")
    val comment = MutableStateFlow("")

    fun setCard(value: CreditCard?) {
        if (value == null) return
        cardholderName.value = value.cardholderName
        cardNumber.value = value.cardNumber
        expiryDate.value = "${value.expiryMonth}${value.expiryYear}"
        cvv.value = value.cvv
    }

}