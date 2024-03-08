package com.liid.dekanatkeys.models

import com.google.gson.annotations.SerializedName

class Classroom(@SerializedName("id") val id: String,
                @SerializedName("building") val building: String,
                @SerializedName("number") val number: String,
                @SerializedName("address") val address: String) {
}