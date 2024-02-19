package com.liid.dekanatkeys.models
import com.google.gson.annotations.SerializedName

data class LoginResponse(@SerializedName("access_token") val token: String) {}