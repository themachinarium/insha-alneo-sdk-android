package com.machinarum.alneo_sdk.utils

import android.content.Context
import android.content.pm.PackageManager
import android.nfc.NfcManager
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.machinarum.alneo_sdk.R

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

    fun deviceHasNfc(context: Context): Boolean {
        // Is NFC adapter present (whether enabled or not)
        val nfcManager = context.getSystemService(Context.NFC_SERVICE) as NfcManager?
        if (nfcManager != null) {
            val adapter = nfcManager.defaultAdapter
            return adapter != null
        }
        return false
    }

    fun validateEmail(email: String?): Boolean {
        return !TextUtils.isEmpty(email) && email?.let { Patterns.EMAIL_ADDRESS.matcher(it).matches() } == true
    }

     fun showMaterialDialog(
        title: String,
        description: String,
        context: Context,
        positiveButtonText: String,
        negativeButtonText: String,
        positiveAction: () -> Unit,
        negativeAction: () -> Unit
    ) {
        val dialog = MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(description)
            .setPositiveButton(positiveButtonText) { dialog, _ ->
                dialog.dismiss()
                positiveAction.invoke()
            }
            .setNegativeButton(negativeButtonText) { dialog, _ ->
                dialog.dismiss()
                negativeAction.invoke()
            }
            .create()

        dialog.setOnShowListener {
            val negativeButtonView = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            negativeButtonView.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.red_a
                )
            ) // Set custom color
        }
        dialog.show()

    }

    fun showBasicSnackbar(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
    }

    fun showSnackbarWithAction(view: View, message: String, actionText: String, action: () -> Unit) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            .setAction(actionText) {
                action()
            }
            .show()
    }

    fun calculateNetPrice(price: Double, commissionRate: Double, percentageBase: Double = 100.0): Double {
        if (commissionRate < 0) {
            throw IllegalArgumentException("Commission rate cannot be negative.")
        }
        return price * percentageBase / (percentageBase + commissionRate)
    }


}