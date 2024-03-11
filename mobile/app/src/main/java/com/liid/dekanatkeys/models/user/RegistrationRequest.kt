package com.liid.dekanatkeys.models.user

import com.google.gson.annotations.SerializedName

data class RegistrationRequest (@SerializedName("email") val email: String,
                                @SerializedName("password") val password: String,
                                @SerializedName("full_name") val name: String)
{}