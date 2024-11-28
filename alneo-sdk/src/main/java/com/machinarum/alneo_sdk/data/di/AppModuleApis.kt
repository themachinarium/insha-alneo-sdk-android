package com.machinarum.alneo_sdk.data.di


import android.content.Context
import com.machinarum.alneo_sdk.data.api.client.AlneoRetrofitClient
import com.machinarum.alneo_sdk.data.api.repository.AlneoRepo
import com.machinarum.alneo_sdk.ui.payment_method.PaymentMethodVM
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