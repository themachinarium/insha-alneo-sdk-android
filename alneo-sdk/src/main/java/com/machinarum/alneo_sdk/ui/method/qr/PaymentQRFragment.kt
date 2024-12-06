package com.machinarum.alneo_sdk.ui.method.qr

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.machinarum.alneo_sdk.databinding.FragmentPaymentQrBinding
import com.machinarum.alneo_sdk.utils.generateQRCode
import com.machinarum.alneo_sdk.utils.navigateSafely
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PaymentQRFragment : Fragment() {

    private val safeArgs: PaymentQRFragmentArgs by navArgs()
    private val viewModel: PaymentQRVM by viewModel {
        parametersOf(safeArgs.price)
    }
    private var _binding: FragmentPaymentQrBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaymentQrBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        generateQrCode("Vusal")
    }

    private fun observeViewModel() {
        viewModel.timer.timeFinished.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().navigateSafely(
                    PaymentQRFragmentDirections.actionPaymentQRFragmentToPaymentQRProcessFragment(
                        safeArgs.price
                    )
                )
            }
        }
    }


    private fun generateQrCode(text: String) {
        val rnds = (0..100).random()
        lifecycleScope.launch(Dispatchers.IO) {
            val qrCode = context.generateQRCode(text + rnds, 500, 500)

            withContext(Dispatchers.Main) {
                binding.imageviewGeneratedQr.setImageBitmap(qrCode)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}