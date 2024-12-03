package com.machinarum.alneo_sdk.data.di


import android.content.Context
import com.machinarum.alneo_sdk.data.api.client.AlneoRetrofitClient
import com.machinarum.alneo_sdk.data.api.repository.AlneoRepo
import com.machinarum.alneo_sdk.ui.method.contactless.PaymentContactlessVM
import com.machinarum.alneo_sdk.ui.method.sms.PaymentSMSVM
import com.machinarum.alneo_sdk.ui.payment_method.PaymentMethodVM
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
        viewModel { PaymentMethodVM(get()) }
        viewModel { InputPaymentPriceVM() }
        viewModel {(price:Long)-> PaymentContactlessVM(get(), argsPrice = price) }
        viewModel {(price:Long)-> PaymentSMSVM(get(), argsPrice = price) }
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