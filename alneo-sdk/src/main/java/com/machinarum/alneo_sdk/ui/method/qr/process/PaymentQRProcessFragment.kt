package com.machinarum.alneo_sdk.ui.method.qr.process

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.machinarum.alneo_sdk.databinding.FragmentPaymentQrProcessBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PaymentQRProcessFragment : Fragment() {

    private val args: PaymentQRProcessFragmentArgs by navArgs()
    private val viewModel: PaymentQRProcessVM by viewModel {
        parametersOf(args.price)
    }
    private var _binding: FragmentPaymentQrProcessBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaymentQrProcessBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}