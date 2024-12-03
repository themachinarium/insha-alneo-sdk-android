package com.machinarum.alneo_sdk.ui.method

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.machinarum.alneo_sdk.R
import com.machinarum.alneo_sdk.databinding.FragmentPaymentMethodBinding
import com.machinarum.alneo_sdk.utils.Helper.deviceHasNfc
import com.machinarum.alneo_sdk.utils.Helper.showMaterialDialog
import com.machinarum.alneo_sdk.utils.navigateSafely
import org.koin.androidx.viewmodel.ext.android.viewModel

class PaymentMethodFragment : Fragment() {
    private val args: PaymentMethodFragmentArgs by navArgs()

    val viewModel: PaymentMethodVM by viewModel()

    private var _binding: FragmentPaymentMethodBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaymentMethodBinding.inflate(inflater, container, false)
        binding.nfcPayment.isVisible = deviceHasNfc(requireContext())
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.nfcPayment.setOnClickListener {

            if (args.price > MAX_PRICE)
                showMaterialDialog(
                    title = getString(R.string.dialog_app_contactless_limit_reached),
                    description = getString(R.string.dialog_app_contactless_limit_reached_description),
                    context = requireContext(),
                    positiveButtonText = getString(R.string.yes_uppercase),
                    negativeButtonText = getString(R.string.no),
                    positiveAction = {

                    },
                    negativeAction = {

                    }
                )
            else
                findNavController().navigateSafely(
                    PaymentMethodFragmentDirections.actionPaymentMethodFragmentToPaymentContactlessFragment(
                        args.price
                    )
                )
        }
        binding.smsPayment.setOnClickListener {
            findNavController().navigateSafely(
                PaymentMethodFragmentDirections.actionPaymentMethodFragmentToPaymentSMSFragment(
                    args.price
                )
            )
        }
        binding.emailPayment.setOnClickListener {
            findNavController().navigateSafely(
                PaymentMethodFragmentDirections.actionPaymentMethodFragmentToPaymentEmailFragment(
                    args.price
                )
            )

        }
    }

    companion object {
        //1500 TL
        const val MAX_PRICE = 150000L

    }
}