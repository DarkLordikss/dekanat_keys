package com.liid.dekanatkeys.models

import com.google.gson.annotations.SerializedName

class MessageResponse(@SerializedName("message") val message: String,
                      @SerializedName("detail") val detail: String) {
}