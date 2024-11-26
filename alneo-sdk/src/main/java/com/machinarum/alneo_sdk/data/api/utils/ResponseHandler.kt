package com.machinarum.alneo_sdk.data.api.utils

import com.google.gson.Gson
import com.google.gson.JsonParser
import com.machinarum.alneo_sdk.data.models.entity.ErrorEntity
import okhttp3.ResponseBody
import retrofit2.HttpException

object ResponseHandler {
    fun <T : Any> handleSuccess(data: T?): Resource<T> {
        return if (data != null)
            Resource.Success(data)
        else
            Resource.Error()
    }

    fun <T : Any> handleException(e: Exception): Resource<T> {

        if (e is HttpException) {
            return handleErrorBody(e.response()?.errorBody()).apply {
                statusCode = e.code()
            }
        }

        return Resource.Error()
    }


    fun handleErrorBody(body: ResponseBody?): Resource.Error {
        try {
            Gson().fromJson(JsonParser.parseString(body?.string()), ErrorEntity::class.java)
                ?.let {

                    val statusEnum = ResponseStatus.find(it.code)
                    return Resource.Error(
                        message = it.message,
                        statusEnum = ResponseStatus.find(it.code)
                    )
                }
        } catch (e: Exception) {
        }

        return Resource.Error()
    }
}

