package com.liid.dekanatkeys.models.user

import com.google.gson.annotations.SerializedName

class UserInfo(
    @SerializedName("id") val id: String,
    @SerializedName("full_name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("role") val role: String){

}

