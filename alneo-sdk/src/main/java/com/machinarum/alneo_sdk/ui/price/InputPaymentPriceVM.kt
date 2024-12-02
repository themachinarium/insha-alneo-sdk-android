package com.machinarum.alneo_sdk.ui.price

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale

class InputPaymentPriceVM : ViewModel() {

    private val _price = MutableStateFlow(0L)
    val price = _price.asStateFlow()

    private val _formattedPrice = MutableStateFlow(DEFAULT_PRICE_FORMAT)
    val formattedPrice = _formattedPrice.asStateFlow()

    private val _navigateToPaymentMethod = MutableSharedFlow<Boolean>()
    val navigateToPaymentMethod = _navigateToPaymentMethod.asSharedFlow()

    companion object {
        const val MAX_PRICE_LENGTH = 10
        const val DEFAULT_PRICE_FORMAT = "0.00 TL"
    }

    fun onNumberButtonClicked(value: Int) {
        val newPrice = _price.value * 10 + value
        if (newPrice.toString().length > MAX_PRICE_LENGTH) return

        updatePrice(newPrice)
    }

    fun onDeleteButtonClicked() {
        val newPrice = _price.value / 10
        updatePrice(newPrice)
    }

    fun nextButtonClicked() = viewModelScope.launch {
        _navigateToPaymentMethod.emit((price.value / 100.0) > 0.0)
    }

    private fun updatePrice(newPrice: Long) {
        _price.value = newPrice
        _formattedPrice.value =
            if (newPrice == 0L) DEFAULT_PRICE_FORMAT else newPrice.toFormattedString()
    }

    private fun Long.toFormattedString(): String {
        val fixedPrice = this / 100.0
        return String.format(Locale.getDefault(), "%.2f TL", fixedPrice)
    }
}

