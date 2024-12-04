package com.machinarum.alneo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.machinarum.alneo.ui.theme.AlneoPOSTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AlneoPOSTheme {
//                AlneoSdkInitializer.initialize(this)
//                AlneoSDK.startPaymentMethodActivity(this)
            }
        }
    }
}