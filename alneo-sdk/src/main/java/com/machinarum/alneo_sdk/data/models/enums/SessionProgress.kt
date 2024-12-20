package com.machinarum.alneo_sdk.data.models.enums;

enum class SessionProgress {
    NONE,
    INITIAL,
    HANSHAKED,
    DEAD,
    PAYMENT_REQUEST_CREATED,            //param tarafindan 3d url olusturuldu
    PAYMENT_REQUEST_FAILED,             //param tarafindan 3d url olusturulamadi
    PAYMENT_REJECTED_BY_CUSTOMER,
    PAYMENT_REJECTED_BY_COMPANY,
    PAYMENT_FAILED,
    PAYMENT_SUCCESS,
    PAYMENT_FAILURE;

}
