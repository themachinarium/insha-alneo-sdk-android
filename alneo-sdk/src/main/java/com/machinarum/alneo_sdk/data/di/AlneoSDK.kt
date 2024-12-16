package com.machinarum.alneo_sdk.data.di

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import com.machinarum.alneo_sdk.R
import com.machinarum.alneo_sdk.ui.MainActivity
import com.machinarum.alneo_sdk.utils.EventBus

object AlneoSDK {
    fun startPaymentMethodActivity(context: Context) {
        val intent = Intent(context, MainActivity::class.java)
        context.startActivity(intent)
    }

    fun startNfc(context: Context, price: Long) {
        val intent = Intent(context, MainActivity::class.java)
        context.startActivity(intent)
        EventBus.navigateDeepLink.value =
            context.resources.getString(R.string.deeplink_nfc).replace("{price}", price.toString())
                .toUri()
    }

    fun cancelNfc(paymentId: String) {
        EventBus.closeDeepLink.value = true
    }
}
