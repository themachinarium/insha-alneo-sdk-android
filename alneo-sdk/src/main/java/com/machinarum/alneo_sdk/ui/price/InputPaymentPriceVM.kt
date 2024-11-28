package com.machinarum.alneo_sdk.ui.price

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.Locale

class InputPaymentPriceVM : ViewModel() {

    private val _price = MutableLiveData(0L)
    val price: LiveData<Long> get() = _price

    private val _formattedPrice = MutableLiveData(DEFAULT_PRICE_FORMAT)
    val formattedPrice: LiveData<String> get() = _formattedPrice

    companion object {
        const val MAX_PRICE_LENGTH = 10
        const val DEFAULT_PRICE_FORMAT = "0.00 TL"
    }

    fun onNumberButtonClicked(value: Int) {
        val newPrice = (_price.value ?: 0L) * 10 + value
        if (newPrice.toString().length > MAX_PRICE_LENGTH) return

        updatePrice(newPrice)
    }

    fun onDeleteButtonClicked() {
        val newPrice = (_price.value ?: 0L) / 10
        updatePrice(newPrice)
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

