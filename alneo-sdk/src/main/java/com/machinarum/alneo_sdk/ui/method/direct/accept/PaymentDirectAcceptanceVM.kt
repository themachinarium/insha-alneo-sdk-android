package com.machinarum.alneo_sdk.ui.method.direct.accept

import androidx.lifecycle.ViewModel
import com.machinarum.alneo_sdk.data.api.repository.AlneoRepo
import com.machinarum.alneo_sdk.data.models.entity.InstallmentEntity
import com.machinarum.alneo_sdk.utils.toFormattedString
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PaymentDirectAcceptanceVM(private val repository: AlneoRepo, argsPrice: Long) : ViewModel() {

    private val _companyName = MutableStateFlow<String>("")
    val companyName = _companyName.asStateFlow()

    private val _companyCategory = MutableStateFlow<String>("")
    val companyCategory = _companyCategory.asStateFlow()

    private val _formattedPrice = MutableStateFlow(argsPrice.toFormattedString())
    val formattedPrice = _formattedPrice.asStateFlow()

    private val _installments = MutableStateFlow<List<InstallmentEntity>>(emptyList())
    val installments = _installments.asStateFlow()




}