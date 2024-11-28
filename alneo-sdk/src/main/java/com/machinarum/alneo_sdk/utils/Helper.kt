package com.machinarum.alneo_sdk.utils

import android.content.Context
import android.content.pm.PackageManager

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
}