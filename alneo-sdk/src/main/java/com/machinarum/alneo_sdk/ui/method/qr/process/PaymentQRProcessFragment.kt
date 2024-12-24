package com.machinarum.alneo_sdk.ui.method.qr.process

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.machinarum.alneo_sdk.R
import com.machinarum.alneo_sdk.data.models.enums.SessionProgress
import com.machinarum.alneo_sdk.databinding.FragmentPaymentQrProcessBinding
import com.machinarum.alneo_sdk.utils.Helper
import com.machinarum.alneo_sdk.utils.view.dialog.DialogB
import com.machinarum.alneo_sdk.utils.view.dialog.DialogType
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PaymentQRProcessFragment : Fragment() {

    private val args: PaymentQRProcessFragmentArgs by navArgs()
    private val viewModel: PaymentQRProcessVM by viewModel {
        parametersOf(args.price, args.sessionToken)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewModel.navigateToBack.collect {
                if (it) {
                    findNavController().navigateUp()
                }
            }
        }

        lifecycleScope.launch {
            viewModel.errorMessages.collect {
                if (it.isNotEmpty())
                    Helper.showBasicSnackbar(binding.root, it)
            }
        }

        lifecycleScope.launch {
            viewModel.sessionProgress.collect {
                when (it) {
                    SessionProgress.DEAD -> {
                        showDialog(
                            title = "Oturum Sonlandı",
                            description = "Ödeme oturumu sonlandığından tahsilat işlemi gerçekleştirilmedi.",
                            onAcceptAction = {
                                findNavController().navigateUp()
                            },
                        )
                    }

                    SessionProgress.PAYMENT_REQUEST_FAILED -> {
                        binding.imageviewStatus.setImageResource(R.drawable.ic_status_error)
                        binding.textviewTitle.setText(getText(R.string.message_payment_process_not_started))
                        binding.textviewDescription.setText(getText(R.string.message_payment_process_not_started_description))
                    }

                    SessionProgress.PAYMENT_REJECTED_BY_CUSTOMER -> {
                        showDialog(
                            title = "İptal Edildi",
                            description = "Müşteri tarafından tahsilat iptal edilmiştir.",
                            onAcceptAction = {
                                findNavController().navigateUp()
                            },
                        )
                    }

                    SessionProgress.PAYMENT_REJECTED_BY_COMPANY -> {
                        showDialog(
                            title = "İptal Edildi",
                            description = "Firma tarafından tahsilat iptal edilmiştir.",
                            onAcceptAction = {
                                findNavController().navigateUp()
                            },
                        )
                    }

                    SessionProgress.PAYMENT_FAILURE -> {
                        binding.imageviewStatus.setImageResource(R.drawable.ic_status_error)
                        binding.nextBtn.visibility = View.VISIBLE
                        binding.cancelButton.visibility = View.GONE
                    }

                    SessionProgress.PAYMENT_SUCCESS -> {
                        binding.imageviewStatus.setImageResource(R.drawable.ic_status_success)
                        binding.textviewTitle.text = getText(R.string.payment_order_created)
                        binding.textviewDescription.text = ""
                        binding.nextBtn.visibility = View.VISIBLE
                        binding.cancelButton.visibility = View.GONE
                    }

                    else -> {

                    }


                }
            }
        }

        binding.cancelButton.setOnClickListener {
            showDialog(
                title = getString(R.string.dialog_payment_cancel),
                dialogType = DialogType.INFO,
                description = getString(R.string.dialog_payment_cancel_description),
                onAcceptAction = {
                    viewModel.rejectPaymentSession()
                }
            )
        }

        binding.nextBtn.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun showDialog(
        title: String?,
        description: String?,
        onAcceptAction: (() -> Unit)? = null,
        dialogType: DialogType = DialogType.ERROR
    ) {
        val dialogA = DialogB(
            context = requireContext(),
            dialogType = dialogType,
            title = title,
            description = description,
            onAcceptButtonClicked = onAcceptAction,
            isRejectButtonVisible = false,
            onAcceptButtonText = getString(R.string.to_ok)
        )
        dialogA.show()
    }

    override fun onResume() {
        super.onResume()
        viewModel.checkPaymentSession()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}