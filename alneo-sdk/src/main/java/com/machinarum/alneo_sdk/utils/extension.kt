package com.machinarum.alneo_sdk.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import timber.log.Timber
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

fun Context?.generateQRCode(
    text: String,
    height: Int,
    width: Int
): Bitmap {

    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val codeWriter = MultiFormatWriter()
    try {
        val bitMatrix = codeWriter.encode(text, BarcodeFormat.QR_CODE, width, height)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
            }
        }
    } catch (e: WriterException) {
        Timber.e("generateQRCode: ${e.message}")
    }
    return bitmap
}