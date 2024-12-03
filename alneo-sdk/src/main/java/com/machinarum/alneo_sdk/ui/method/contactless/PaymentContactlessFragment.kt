package com.machinarum.alneo_sdk.ui.method.contactless

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.machinarum.alneo_sdk.R
import com.machinarum.alneo_sdk.databinding.FragmentPaymentContactlessBinding
import com.machinarum.alneo_sdk.utils.Helper
import com.machinarum.alneo_sdk.utils.Helper.isPackageInstalled
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PaymentContactlessFragment : Fragment() {

    private val args: PaymentContactlessFragmentArgs by navArgs()

    private val viewModel: PaymentContactlessVM by viewModel {
        parametersOf(args.price)
    }

    private var _binding: FragmentPaymentContactlessBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaymentContactlessBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewModel.loading.collect {
                binding.loading.visibility = if (it) View.VISIBLE else View.GONE
            }
        }

        lifecycleScope.launch {
            viewModel.errorMessage.collect {
                if (it.isNullOrEmpty()) return@collect
                Helper.showBasicSnackbar(binding.root, it)
            }
        }

        binding.nextBtn.setOnClickListener {
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
        if (!isPackageInstalled(contactlessAppPackageName, requireContext())) {

            Helper.showMaterialDialog(
                title = getString(R.string.dialog_app_contactless_not_available),
                description = getString(R.string.dialog_app_contactless_not_available_description),
                context = requireContext(),
                positiveButtonText = getString(R.string.yes_uppercase),
                negativeButtonText = getString(R.string.no),
                positiveAction = {
                    openAppStore()
                },
                negativeAction = {

                }
            )
        }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        val contactlessAppPackageName = "com.provisionpay.softpos.alneo"

    }

}