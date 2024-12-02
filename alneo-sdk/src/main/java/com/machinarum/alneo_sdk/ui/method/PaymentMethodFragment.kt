package com.machinarum.alneo_sdk.ui.method

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.machinarum.alneo_sdk.databinding.FragmentPaymentMethodBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class PaymentMethodFragment : Fragment() {

    val viewModel: PaymentMethodVM by viewModel()
    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentPaymentMethodBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }
}