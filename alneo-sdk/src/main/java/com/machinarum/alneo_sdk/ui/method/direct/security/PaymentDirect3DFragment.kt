package com.machinarum.alneo_sdk.ui.method.direct.security

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.machinarum.alneo_sdk.R
import com.machinarum.alneo_sdk.databinding.FragmentPaymentDirect3dBinding
import com.machinarum.alneo_sdk.databinding.FragmentPaymentDirectBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class PaymentDirect3DFragment : Fragment() {


    private val viewModel: PaymentDirect3DVM by viewModel()

    private var _binding: FragmentPaymentDirect3dBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaymentDirect3dBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
}