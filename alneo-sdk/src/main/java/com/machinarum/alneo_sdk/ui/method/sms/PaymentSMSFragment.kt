package com.machinarum.alneo_sdk.ui.method.sms

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.machinarum.alneo_sdk.R
import com.machinarum.alneo_sdk.databinding.FragmentPaymentSmsBinding
import com.machinarum.alneo_sdk.utils.Helper
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
        binding.nextBtn.setOnClickListener {
            var phoneNumber: String = binding.inputEdittextPhoneNumber.text.toString()
            if (!TextUtils.isEmpty(phoneNumber)) {
                phoneNumber = phoneNumber.replace(" ".toRegex(), "")
            }

            if (phoneNumber.isEmpty()) {
                Helper.showBasicSnackbar(
                    binding.root,
                    getString(R.string.message_phone_number_can_not_be_empty)
                )
                return@setOnClickListener
            }
            if (phoneNumber.length < 11) {
                Helper.showBasicSnackbar(
                    binding.root,

                    getString(R.string.message_fit_phone_number_digits)
                )
                return@setOnClickListener
            }

            return@setOnClickListener
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}