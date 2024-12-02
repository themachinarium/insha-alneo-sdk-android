package com.machinarum.alneo_sdk.utils

import android.content.Context
import android.content.pm.PackageManager
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavDirections

object Helper {

    fun isPackageInstalled(packageName: String?, context: Context): Boolean {
        try {
            val packageManager = context.packageManager
            packageManager.getPackageInfo(packageName!!, 0)
            return true
        } catch (e: PackageManager.NameNotFoundException) {
            return false
        }
    }

    fun NavController.navigateSafely(@IdRes actionId: Int) {
        currentDestination?.getAction(actionId)?.let { navigate(actionId) }
    }

    fun NavController.navigateSafely(directions: NavDirections) {
        currentDestination?.getAction(directions.actionId)?.let { navigate(directions) }
    }
}