package com.machinarum.alneo_sdk.data.api.utils

enum class ResponseStatus {
    OK,
    FAILED;

    companion object {
        fun find(status: String?) =
            entries.find { it.name.lowercase() == status?.lowercase() } ?: FAILED
    }
}