package com.machinarum.alneo_sdk.ui.method.qr

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.machinarum.alneo_sdk.R
import com.machinarum.alneo_sdk.data.models.enums.SessionProgress
import com.machinarum.alneo_sdk.databinding.FragmentPaymentQrBinding
import com.machinarum.alneo_sdk.utils.Helper
import com.machinarum.alneo_sdk.utils.generateQRCode
import com.machinarum.alneo_sdk.utils.navigateSafely
import com.machinarum.alneo_sdk.utils.view.dialog.DialogB
import com.machinarum.alneo_sdk.utils.view.dialog.DialogType
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
    }

    private fun observeViewModel() {
        viewModel.timer.timeFinished.observe(viewLifecycleOwner) {
            if (it) {
                viewModel.finalizePaymentSession()
                viewModel.createPaymentSession()
            }
        }

        lifecycleScope.launch {
            viewModel.errorMessages.collect {
                if (it.isNotEmpty())
                    Helper.showBasicSnackbar(binding.root, it)
            }
        }

        lifecycleScope.launch {
            viewModel.sessionToken.collect {
                if (it.isNotEmpty())
                    generateQrCode(it)
            }
        }

        lifecycleScope.launch {
            viewModel.navigateToProgress.collect {
                if (it) {
                    viewModel.finalizePaymentSession()
                    findNavController().navigateSafely(
                        PaymentQRFragmentDirections.actionPaymentQRFragmentToPaymentQRProcessFragment(
                            price = safeArgs.price,
                            sessionToken = viewModel.sessionToken.value
                        )
                    )
                }
            }
        }

        lifecycleScope.launch {
            viewModel.sessionProgress.collect { sessionProgress ->
                when (sessionProgress) {
                    SessionProgress.DEAD,
                    SessionProgress.PAYMENT_REQUEST_FAILED -> {
                        val (title, description) = when (sessionProgress) {
                            SessionProgress.DEAD -> "Oturum Sonlandı" to
                                    "Ödeme oturumu sonlandığından tahsilat işlemi gerçekleştirilmedi."

                            SessionProgress.PAYMENT_REQUEST_FAILED -> "Tahsilat Oturumu Sonlandı" to
                                    "Tahsilat oturumu bir hata ile karşılaştığından yeni bir oturum açılacaktır."

                            else -> null to null // This will never occur due to the outer when condition
                        }

                        showErrorDialog(title, description) {
                            viewModel.timer.stop()
                            viewModel.createPaymentSession()
                        }
                    }

                    else -> {
                        // Handle other cases if needed
                    }
                }
            }
        }
    }

    /**
     * Reusable function to show an error dialog
     */
    private fun showErrorDialog(
        title: String?,
        description: String?,
        onAcceptAction: (() -> Unit)? = null
    ) {
        val dialogA = DialogB(
            context = requireContext(),
            dialogType = DialogType.ERROR,
            title = title,
            description = description,
            onAcceptButtonClicked = onAcceptAction,
            isRejectButtonVisible = false,
            onAcceptButtonText = getString(R.string.to_ok)
        )
        dialogA.show()
    }


    private fun generateQrCode(text: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            val qrCode = context.generateQRCode(text, 500, 500)

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