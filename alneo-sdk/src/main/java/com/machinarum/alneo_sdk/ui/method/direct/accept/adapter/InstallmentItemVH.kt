package com.machinarum.alneo_sdk.ui.method.direct.accept.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.machinarum.alneo_sdk.data.models.entity.InstallmentEntity
import com.machinarum.alneo_sdk.databinding.RowInstallmentBinding


class InstallmentItemVH(private val binding: RowInstallmentBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        dto: InstallmentEntity,
        onItemClick: (InstallmentEntity) -> Unit,
    ) {
        binding.model = dto

        binding.root.setOnClickListener {
            onItemClick.invoke(dto)
        }

        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): InstallmentItemVH {
            val binding = RowInstallmentBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )

            return InstallmentItemVH(binding)
        }
    }
}