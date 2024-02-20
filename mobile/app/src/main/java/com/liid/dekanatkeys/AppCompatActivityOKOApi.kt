package com.liid.dekanatkeys

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.liid.dekanatkeys.models.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

open class AppCompatActivityOKOApi : AppCompatActivity() {

    protected lateinit var okoapi: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val retrofit = Retrofit.Builder()
            .baseUrl("http://89.23.106.97:8118/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        okoapi = retrofit.create(ApiService::class.java)
    }
}