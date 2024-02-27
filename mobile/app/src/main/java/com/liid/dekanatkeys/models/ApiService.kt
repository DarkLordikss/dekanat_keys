package com.liid.dekanatkeys.models

import com.liid.dekanatkeys.models.user.LoginRequest
import com.liid.dekanatkeys.models.user.LoginResponse
import com.liid.dekanatkeys.models.user.RegistrationRequest
import com.liid.dekanatkeys.models.user.RegistrationResponse
import com.liid.dekanatkeys.models.user.UserProfile
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("user/login/") // замените "login" на путь к вашему API для входа
    fun loginUser(@Body request: LoginRequest): Call<LoginResponse>
    @POST("user/register/")
    fun registrationUser(@Body request: RegistrationRequest): Call<RegistrationResponse>
    @GET("user/")
    fun fetchUserProfile(@Header("Authorization") token: String): Call<UserProfile>


}