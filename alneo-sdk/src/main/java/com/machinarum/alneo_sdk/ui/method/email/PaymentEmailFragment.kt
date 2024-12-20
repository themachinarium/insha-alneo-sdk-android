package com.machinarum.alneo_sdk.ui.method.email

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.machinarum.alneo_sdk.data.models.enums.PaymentType
import com.machinarum.alneo_sdk.databinding.FragmentPaymentEmailBinding
import com.machinarum.alneo_sdk.utils.Helper
import com.machinarum.alneo_sdk.utils.navigateSafely
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PaymentEmailFragment : Fragment() {

    private val args: PaymentEmailFragmentArgs by navArgs()
    private val viewModel: PaymentEmailVM by viewModel {
        parametersOf(args.price)
    }
    private var _binding: FragmentPaymentEmailBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaymentEmailBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        binding.nextBtn.setOnClickListener {
            viewModel.validateEmail(context = requireContext())
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.emailError.collect {
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
                        PaymentEmailFragmentDirections.actionPaymentEmailFragmentToPaymentProcessFragment(
                            args.price, PaymentType.LINK,
                            desc = viewModel.description.value,
                            data = viewModel.email.value
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