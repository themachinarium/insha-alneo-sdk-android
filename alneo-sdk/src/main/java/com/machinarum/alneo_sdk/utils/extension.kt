package com.machinarum.alneo_sdk.utils

import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import java.util.Locale

fun NavController.navigateSafely(@IdRes actionId: Int) {
    currentDestination?.getAction(actionId)?.let { navigate(actionId) }
}

fun NavController.navigateSafely(directions: NavDirections) {
    currentDestination?.getAction(directions.actionId)?.let { navigate(directions) }
}

fun Long.toFormattedString(): String {
    val fixedPrice = this / 100.0
    return String.format(Locale.getDefault(), "%.2f TL", fixedPrice)
}