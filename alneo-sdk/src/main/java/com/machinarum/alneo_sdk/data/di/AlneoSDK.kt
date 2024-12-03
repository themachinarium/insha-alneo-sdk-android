package com.machinarum.alneo_sdk.data.di

import android.content.Context
import android.content.Intent
import com.machinarum.alneo_sdk.ui.MainActivity
import com.machinarum.alneo_sdk.ui.payment_method.PaymentMethodActivity

object AlneoSDK {
    fun startPaymentMethodActivity(context: Context) {
        val intent = Intent(context, MainActivity::class.java)
        context.startActivity(intent)
    }
}
