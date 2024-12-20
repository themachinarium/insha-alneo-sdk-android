package com.machinarum.alneo_sdk.data.di


import android.content.Context
import com.machinarum.alneo_sdk.data.api.client.AlneoRetrofitClient
import com.machinarum.alneo_sdk.data.api.repository.AlneoRepo
import com.machinarum.alneo_sdk.ui.method.contactless.PaymentContactlessVM
import com.machinarum.alneo_sdk.ui.method.direct.PaymentDirectVM
import com.machinarum.alneo_sdk.ui.method.direct.accept.PaymentDirectAcceptanceVM
import com.machinarum.alneo_sdk.ui.method.direct.security.PaymentDirect3DVM
import com.machinarum.alneo_sdk.ui.method.email.PaymentEmailVM
import com.machinarum.alneo_sdk.ui.method.qr.PaymentQRVM
import com.machinarum.alneo_sdk.ui.method.qr.process.PaymentQRProcessVM
import com.machinarum.alneo_sdk.ui.method.sms.PaymentSMSVM
import com.machinarum.alneo_sdk.ui.method.sms.process.PaymentProcessVM
import com.machinarum.alneo_sdk.ui.price.InputPaymentPriceVM
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module

object AlneoSdkInitializer {
    private lateinit var appContext: Context

    private val sdkModule = module {
        single { AlneoRetrofitClient.provideAlneoApi(appContext) }
        factory { AlneoRepo(get()) }
        viewModel { InputPaymentPriceVM() }
        viewModel { (price: Long) -> PaymentContactlessVM(get(), argsPrice = price) }
        viewModel { (price: Long) -> PaymentSMSVM(get(), argsPrice = price) }
        viewModel { (price: Long) -> PaymentEmailVM(get(), argsPrice = price) }
        viewModel { (price: Long) -> PaymentProcessVM(get(), argsPrice = price) }
        viewModel { (price: Long) -> PaymentQRVM(get(), argsPrice = price) }
        viewModel { (price: Long, sessionToken: String) ->
            PaymentQRProcessVM(
                get(),
                argsPrice = price,
                sessionToken = sessionToken
            )
        }
        viewModel { (price: Long) -> PaymentDirectVM(get(), argsPrice = price) }
        viewModel { (price: Long) -> PaymentDirectAcceptanceVM(get(), argsPrice = price) }
        viewModel { PaymentDirect3DVM() }
    }

    fun initialize(context: Context) {
        appContext = context
        if (GlobalContext.getOrNull() == null) {
            startKoin {
                androidContext(context)
                modules(sdkModule)
            }
        }
    }

    fun stop() {
        stopKoin()
    }
}