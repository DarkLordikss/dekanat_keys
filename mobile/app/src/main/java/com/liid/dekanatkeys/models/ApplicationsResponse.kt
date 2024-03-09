package com.liid.dekanatkeys.models

import com.google.gson.annotations.SerializedName
import com.liid.dekanatkeys.helpers.Log
import java.time.LocalDate
import java.util.UUID

class ApplicationsResponse(@SerializedName("schedule") val TimetableWithDates: List<TimetableWithDate>) {

}

class Timetable(@SerializedName("1") val application1: List<Application>?,
                @SerializedName("2") val application2: List<Application>?,
                @SerializedName("3") val application3: List<Application>?,
                @SerializedName("4") val application4: List<Application>?,
                @SerializedName("5") val application5: List<Application>?,
                @SerializedName("6") val application6: List<Application>?,
                @SerializedName("7") val application7: List<Application>?,
){

}

class TimetableWithList(timetable: Timetable){
    var applications = listOf<Application?>(
        timetable.application1?.get(0),
        timetable.application2?.get(0),
        timetable.application3?.get(0),
        timetable.application4?.get(0),
        timetable.application5?.get(0),
        timetable.application6?.get(0),
        timetable.application7?.get(0),
    )
}

class TimetableWithDate(@SerializedName("timetable") val timetable: Timetable,
                        @SerializedName("date") val date: String){

}


class Application(@SerializedName("classroom_id") val id: UUID,
                  @SerializedName("name") val name: String,
                  @SerializedName("description") val description: String,
                  @SerializedName("buildings") val building: Int,
                  @SerializedName("class_number") val classNumber: Int){

}