package com.machinarum.alneo_sdk.utils.view.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import androidx.core.view.isVisible
import com.machinarum.alneo_sdk.R
import com.machinarum.alneo_sdk.databinding.DialogBBinding


class DialogB(
    context: Context,
    dialogType: DialogType,
    title: String?,
    description: String?,
    onAcceptButtonClicked: (() -> Unit)? = null,
    onAcceptButtonText: String? = null,
    onRejectButtonClicked: (() -> Unit)? = null,
    isRejectButtonVisible: Boolean = false
) : Dialog(context) {


    // View Binding
    private val binding: DialogBBinding by lazy {
        DialogBBinding.inflate(LayoutInflater.from(context))
    }

    init {
        // Remove dialog title
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        // Set content view to binding root
        setContentView(binding.root)

        // Dialog configurations
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val params = window?.attributes
        params?.width = WindowManager.LayoutParams.MATCH_PARENT
        window?.attributes = params
        setCanceledOnTouchOutside(false)

        // Set title and description
        binding.textviewTitle.text = title
        binding.textviewDescription.text = description

        // Set status icon based on dialog type
        binding.imageviewStatus.setImageResource(
            when (dialogType) {
                DialogType.SUCCESS -> R.drawable.ic_status_success
                DialogType.INFO -> R.drawable.ic_status_info
                DialogType.ERROR -> R.drawable.ic_status_error
                else -> R.drawable.ic_status_error
            }
        )

        // Accept button click listener
        binding.relativelayoutAccept.setOnClickListener {
            onAcceptButtonClicked?.invoke()
            dismiss()
        }

        binding.acceptBtn.text = onAcceptButtonText ?: context.getString(R.string.to_ok)

        // Reject button click listener
        binding.relativelayoutReject.setOnClickListener {
            onRejectButtonClicked?.invoke()
            dismiss()
        }

        binding.relativelayoutReject.isVisible = isRejectButtonVisible
    }

}
