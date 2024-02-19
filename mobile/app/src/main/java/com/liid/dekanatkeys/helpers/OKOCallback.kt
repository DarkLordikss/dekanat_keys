package com.liid.dekanatkeys.helpers

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OKOCallback <T>(
    private val successCallback: (Response<T>) -> Unit,
    private val errorCallback: (Response<T>) -> Unit
) : Callback<T> {

    override fun onResponse(call: Call<T>, response: Response<T>) {
        when {
            response.isSuccessful -> successCallback(response)
            else -> errorCallback(response)
        }
    }

    override fun onFailure(call: Call<T>, t: Throwable) {
        Log("Network Error: " + (t.message ?: "Unknown error"))
    }
}