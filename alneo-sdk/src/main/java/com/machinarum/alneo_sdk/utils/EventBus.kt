package com.machinarum.alneo_sdk.utils

import android.net.Uri

object EventBus {

    val navigateDeepLink = SingleLiveEvent<Uri?>()
}