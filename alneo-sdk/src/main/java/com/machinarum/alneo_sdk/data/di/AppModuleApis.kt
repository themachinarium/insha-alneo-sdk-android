package com.machinarum.alneo_sdk.data.di


import android.content.Context
import com.machinarum.alneo_sdk.data.api.client.AlneoRetrofitClient
import com.machinarum.alneo_sdk.data.api.repository.AlneoRepo
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module

object AlneoSdkInitializer {
    private val sdkModule = module {
        single { AlneoRetrofitClient.provideAlneoApi() }
        factory { AlneoRepo(get()) }
    }

    fun initialize(context: Context) {
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