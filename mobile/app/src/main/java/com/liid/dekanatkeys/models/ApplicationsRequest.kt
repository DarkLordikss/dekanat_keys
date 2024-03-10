package com.liid.dekanatkeys.models

import com.google.gson.annotations.SerializedName
import java.time.LocalDate

class ApplicationsRequest(@SerializedName("building") val building: Int,
                          @SerializedName("start_date") val start_date : LocalDate,
                          @SerializedName("end_date") val end_date : LocalDate,
                          private val classroom :Int) {
    @SerializedName("statuses") public val statuses = listOf(2, 3)
    @SerializedName("classrooms") public val classrooms = listOf(classroom)
}