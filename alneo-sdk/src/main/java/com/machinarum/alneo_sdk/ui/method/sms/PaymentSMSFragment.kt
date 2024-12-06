package com.machinarum.alneo_sdk.ui.method.sms

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.machinarum.alneo_sdk.R
import com.machinarum.alneo_sdk.data.models.enums.PaymentType
import com.machinarum.alneo_sdk.databinding.FragmentPaymentSmsBinding
import com.machinarum.alneo_sdk.utils.Helper
import com.machinarum.alneo_sdk.utils.navigateSafely
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PaymentSMSFragment : Fragment() {

    private val args: PaymentSMSFragmentArgs by navArgs()
    private val viewModel: PaymentSMSVM by viewModel {
        parametersOf(args.price)
    }
    private var _binding: FragmentPaymentSmsBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaymentSmsBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        binding.nextBtn.setOnClickListener {
            viewModel.validatePhoneNumber(requireContext())
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.phoneNumberError.collect {
                if (it != null)
                    Helper.showBasicSnackbar(
                        binding.root,
                        it
                    )
            }
        }
        lifecycleScope.launch {

            viewModel.navigateToPaymentProcess.collect {
                if (it)
                    findNavController().navigateSafely(
                        PaymentSMSFragmentDirections.actionPaymentSMSFragmentToPaymentProcessFragment(
                            args.price, PaymentType.SMS
                        )
                    )
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}