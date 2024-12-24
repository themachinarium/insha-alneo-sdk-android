package com.machinarum.alneo_sdk.ui.method.direct

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.machinarum.alneo_sdk.databinding.FragmentPaymentDirectBinding
import com.machinarum.alneo_sdk.utils.navigateSafely
import io.card.payment.CardIOActivity
import io.card.payment.CreditCard
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class PaymentDirectFragment : Fragment() {

    private val safeArgs: PaymentDirectFragmentArgs by navArgs()
    private val viewModel: PaymentDirectVM by viewModel {
        parametersOf(safeArgs.price)
    }
    private var _binding: FragmentPaymentDirectBinding? = null
    private val binding get() = _binding!!

    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == 13274384) {
                val extraData =
                    it.data?.getParcelableExtra<CreditCard>(CardIOActivity.EXTRA_SCAN_RESULT)
                viewModel.setCard(value = extraData)
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaymentDirectBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }


    private fun scanCard() {
        val scanIntent = Intent(
            requireContext(),
            CardIOActivity::class.java
        )


        // customize these values to suit your needs.
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true) // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, true) // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false) // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CARDHOLDER_NAME, true) // default: false

        scanIntent.putExtra(CardIOActivity.EXTRA_USE_PAYPAL_ACTIONBAR_ICON, false) // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_HIDE_CARDIO_LOGO, true) // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_USE_CARDIO_LOGO, false) // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, true) // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_KEEP_APPLICATION_THEME, true) // default: false

        getResult.launch(scanIntent)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.nextBtn.setOnClickListener {
            findNavController().navigateSafely(
                PaymentDirectFragmentDirections.actionPaymentDirectFragmentToPaymentDirectAcceptanceFragment(
                    safeArgs.price
                )
            )
        }

        binding.scanCard.setOnClickListener {
            scanCard()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val MY_SCAN_REQUEST_CODE = 13274384
    }

}