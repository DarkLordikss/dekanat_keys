package com.liid.dekanatkeys.models

import com.google.gson.annotations.SerializedName
import java.time.LocalDate

class CreateApplicationRequest(@SerializedName("classroom_id") val id: String,
                               @SerializedName("name") val name: String,
                               @SerializedName("description") val description: String,
                               @SerializedName("class_date") val date: String,
                               @SerializedName("time_table_id") val lessonNumber: Int,
                               @SerializedName("dublicates") val dublicates: Int = 1) {
}