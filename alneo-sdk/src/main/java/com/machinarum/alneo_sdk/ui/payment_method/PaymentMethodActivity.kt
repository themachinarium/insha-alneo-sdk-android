package com.machinarum.alneo_sdk.ui.payment_method

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.machinarum.alneo_sdk.BuildConfig
import com.machinarum.alneo_sdk.R
import com.machinarum.alneo_sdk.databinding.ActivityPaymentMethodBinding
import com.machinarum.alneo_sdk.utils.Helper.isPackageInstalled
import org.koin.androidx.viewmodel.ext.android.viewModel

class PaymentMethodActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityPaymentMethodBinding.inflate(layoutInflater)
    }

    private val viewModel by viewModel<PaymentMethodVM>()

    private val contactlessAppPackageName = "com.provisionpay.softpos.alneo"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.apply {
            lifecycleOwner = this@PaymentMethodActivity
            vm = viewModel
        }

        binding.nfcItem.setOnClickListener {
            openNFC()
        }
    }

    override fun onResume() {
        super.onResume()
        checkNFC()
    }

    private fun openNFC() {
        //ok go on
        //adb shell am start -a android.intent.action.VIEW -d https://alneo.com.tr/payment?paymentSessionToken=666DD084DBBFD279
        val browserIntent = Intent(
            Intent.ACTION_VIEW, Uri.parse(
                "https://alneo.com.tr/payment?paymentSessionToken=${viewModel.sessionToken.value}"
            )
        )
        if (Build.VERSION.SDK_INT >= 31) {
            browserIntent.addCategory(Intent.CATEGORY_BROWSABLE)
            browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_REQUIRE_NON_BROWSER)
        }

        startActivity(browserIntent)
    }

    private fun checkNFC() {
        if (!isPackageInstalled(contactlessAppPackageName, this)) {
            showMaterialDialog()
        }
    }

    private fun showMaterialDialog() {
        val dialog = MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.dialog_app_contactless_not_available))
            .setMessage(getString(R.string.dialog_app_contactless_not_available_description))
            .setPositiveButton(getString(R.string.yes_uppercase)) { dialog, _ ->
                dialog.dismiss()
                openAppStore()
            }
            .setNegativeButton(getString(R.string.no)) { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        dialog.setOnShowListener {
            val negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            negativeButton.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.red_a
                )
            ) // Set custom color
        }
        dialog.show()

    }

    private fun openAppStore() {
        try {
            val appStoreIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("market://details?id=" + contactlessAppPackageName)
            )
            appStoreIntent.setPackage("com.android.vending")

            startActivity(appStoreIntent)
        } catch (exception: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=" + contactlessAppPackageName)
                )
            )
        }
    }
}
