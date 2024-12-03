package com.machinarum.alneo_sdk.ui.price

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.machinarum.alneo_sdk.databinding.FragmentInputPaymentPriceBinding
import com.machinarum.alneo_sdk.utils.navigateSafely
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class InputPaymentPriceFragment : Fragment() {

    private var _binding: FragmentInputPaymentPriceBinding? = null
    private val binding get() = _binding!!

    private val viewModel: InputPaymentPriceVM by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentInputPaymentPriceBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.navigateToPaymentMethod.collect {
                if (it)
                    findNavController().navigateSafely(
                        InputPaymentPriceFragmentDirections
                            .actionInputPaymentPriceFragmentToPaymentMethodFragment(viewModel.price.value)
                    )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}