package com.machinarum.alneo_sdk.ui.method.sms.process

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.machinarum.alneo_sdk.R
import com.machinarum.alneo_sdk.data.models.enums.PaymentStatus
import com.machinarum.alneo_sdk.data.models.enums.PaymentType
import com.machinarum.alneo_sdk.databinding.FragmentPaymentProcessBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PaymentProcessFragment : Fragment() {

    private val safeArgs: PaymentProcessFragmentArgs by navArgs()
    private val viewModel: PaymentProcessVM by viewModel {
        parametersOf(safeArgs.price)
    }

    private var _binding: FragmentPaymentProcessBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaymentProcessBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textviewPaymentType.setText(safeArgs.paymentType.textRes)
        lifecycleScope.launch {
            viewModel.paymentStatus.collect {
                val text = when (it) {
                    PaymentStatus.SUCCESS -> {
                        if (safeArgs.paymentType == PaymentType.SMS) {
                            R.string.payment_order_sent_to_customer_via_sms
                        } else {
                            R.string.payment_order_sent_to_customer_via_link
                        }
                    }

                    PaymentStatus.ERROR -> {
                        R.string.message_payment_process_not_started_description
                    }

                    PaymentStatus.LOADING -> {
                        R.string.payment_status_description
                    }

                }
                binding.textviewDescription.setText(text)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}