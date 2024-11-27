package com.machinarum.alneo_sdk.data.api.utils

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.machinarum.alneo_sdk.data.models.entity.ErrorEntity
import okhttp3.ResponseBody
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

object ResponseHandler {
    fun <T : Any> handleSuccess(data: T?): Resource<T> {
        return if (data != null)
            Resource.Success(data)
        else
            Resource.Error()
    }

    fun <T : Any> handleException(e: Exception): Resource<T> {
        return when (e) {
            is HttpException -> handleErrorBody(e.response()?.errorBody()).apply {
                statusCode = e.code()
            }
            is SocketTimeoutException -> Resource.Error(message = "Request timed out.")
            is IOException -> Resource.Error(message = "Network error. Please check your connection.")
            else -> Resource.Error(message = "Something went wrong.")
        }
    }


    fun handleErrorBody(body: ResponseBody?): Resource.Error {
        return try {
            val bodyString = body?.string() ?: throw IllegalStateException("Response body is null")
            Gson().fromJson(bodyString, ErrorEntity::class.java)?.let {
                val statusEnum = ResponseStatus.find(it.code)
                return Resource.Error(
                    message = it.message,
                    statusEnum = statusEnum
                )
            }
            Resource.Error(message = "Failed to parse error body.")
        } catch (e: Exception) {
            Resource.Error(message = "Unexpected error occurred.")
        }
    }}

