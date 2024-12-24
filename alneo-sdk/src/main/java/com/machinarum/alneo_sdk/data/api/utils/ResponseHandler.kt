package com.machinarum.alneo_sdk.data.api.utils

import com.google.gson.Gson
import com.machinarum.alneo_sdk.data.models.response.BaseResponse
import okhttp3.ResponseBody
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

object ResponseHandler {
    fun <T : Any> handleResponse(baseResponse: BaseResponse<T>): Resource<T> {
        return if (baseResponse.code in 200..299) {
            Resource.Success(baseResponse.content ?: throw IllegalStateException("Response data is null"))
        } else {
            Resource.Error(
                message = baseResponse.message ?: "Unknown error occurred.",
                statusCode = baseResponse.code?:500
            )
        }
    }

    fun <T : Any> handleException(e: Exception): Resource<T> {
        return when (e) {
            is HttpException -> handleErrorBody(e.response()?.errorBody())
            is SocketTimeoutException -> Resource.Error(message = "Request timed out.")
            is IOException -> Resource.Error(message = "Network error. Please check your connection.")
            else -> Resource.Error(message = "Something went wrong.")
        }
    }

     fun <T : Any> handleErrorBody(body: ResponseBody?): Resource<T> {
        return try {
            val bodyString = body?.string() ?: throw IllegalStateException("Response body is null")
            val baseResponse = Gson().fromJson(bodyString, BaseResponse::class.java)

            Resource.Error(
                message = baseResponse.message ?: "Unknown error occurred.",
                statusCode = baseResponse.code?:400
            )
        } catch (e: Exception) {
            Resource.Error(message = "Failed to parse error response.")
        }
    }
}

