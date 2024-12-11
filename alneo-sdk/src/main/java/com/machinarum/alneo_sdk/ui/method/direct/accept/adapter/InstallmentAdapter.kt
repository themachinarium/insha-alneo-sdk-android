package com.machinarum.alneo_sdk.ui.method.direct.accept.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.machinarum.alneo_sdk.data.models.entity.InstallmentEntity

class InstallmentAdapter() :
    ListAdapter<InstallmentEntity, RecyclerView.ViewHolder>(DiffCallback) {

    var onItemClick: (InstallmentEntity) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        InstallmentItemVH.from(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is InstallmentItemVH -> {
                holder.bind(
                    getItem(position),
                    onItemClick = onItemClick
                )
            }
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<InstallmentEntity>() {
        override fun areItemsTheSame(oldItem: InstallmentEntity, newItem: InstallmentEntity) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: InstallmentEntity,
            newItem: InstallmentEntity
        ) = oldItem == newItem
    }

}