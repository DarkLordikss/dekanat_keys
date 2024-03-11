package com.liid.dekanatkeys.models

import com.google.gson.annotations.SerializedName

class TransferKeySocketMessage(@SerializedName("application_id") val application_id: String,
                               @SerializedName("user_sender_id") val user_sender_id: String) {
}