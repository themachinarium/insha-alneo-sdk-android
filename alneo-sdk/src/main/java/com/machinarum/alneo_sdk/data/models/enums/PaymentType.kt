package com.machinarum.alneo_sdk.data.models.enums

import com.machinarum.alneo_sdk.R

enum class PaymentType(val textRes: Int) {
    DEFAULT(R.string.undefined),
    QR(R.string.payment_method_qr),
    DIRECT(R.string.payment_method_direct),
    EMAIL(R.string.payment_method_email),
    SMS(R.string.payment_method_sms),
    CONTACTLESS(R.string.payment_method_nfc)
}