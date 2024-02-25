package com.liid.dekanatkeys.helpers

import com.liid.dekanatkeys.models.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


// синглтон для упрощения использования Api, теперь не придется в каждом месте, где нужно Api подключаться к серверу

// val call: Call<LoginResponse> = OKOApiSingleton.api.loginUser(loginRequest) // пример использования, LoginActivity.kt метод login строка 49
object OKOApiSingleton {
    private lateinit var Url: String

    val api: ApiService by lazy {
        Log("create connection to api with customUrl: $Url")
        val retrofit = Retrofit.Builder()
            .baseUrl(Url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(ApiService::class.java)
    }

    fun initialize(Url: String) {
        this.Url = Url
    }
}
