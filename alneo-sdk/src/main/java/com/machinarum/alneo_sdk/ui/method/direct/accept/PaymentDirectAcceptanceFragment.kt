package com.machinarum.alneo_sdk.ui.method.direct.accept

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.machinarum.alneo_sdk.databinding.FragmentPaymentDirectAcceptanceBinding
import com.machinarum.alneo_sdk.ui.method.direct.accept.adapter.InstallmentAdapter
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PaymentDirectAcceptanceFragment : Fragment() {

    private val safeArgs: PaymentDirectAcceptanceFragmentArgs by navArgs()
    private val viewModel: PaymentDirectAcceptanceVM by viewModel{
        parametersOf(safeArgs.price)
    }

    private var _binding: FragmentPaymentDirectAcceptanceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaymentDirectAcceptanceBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe()
    }

    private fun observe() {
        lifecycleScope.launch {
            viewModel.installments.collect {
                adapter.submitList(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val adapter by lazy {
        InstallmentAdapter().also {
            binding.recylerview.adapter = it
        }

    }
}