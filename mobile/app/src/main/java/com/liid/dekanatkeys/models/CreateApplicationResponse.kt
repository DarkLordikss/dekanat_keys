package com.liid.dekanatkeys.models

import com.google.gson.annotations.SerializedName

class CreateApplicationResponse(@SerializedName("message") val message: String,
                                @SerializedName("detail") val detail: String) {
}