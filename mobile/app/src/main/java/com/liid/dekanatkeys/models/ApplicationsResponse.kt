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
class ApplicationWithDateStatus(
    @SerializedName("application_id") val application_id: String,
    @SerializedName("classroom_id") val classroom_id: String,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("buildings") val building: Int,
    @SerializedName("class_number") val classNumber: Int,
    @SerializedName("class_date")val date: String,
    @SerializedName("status") val status: Int,
    @SerializedName("time_table_id")val lessonNumber: Int){

    var userSenderId :String? = null

    companion object{
        fun getFromTimetableWithDates(TimetableWithDates: List<TimetableWithDate>) : List<ApplicationWithDateStatus>{
            var out = mutableListOf<ApplicationWithDateStatus>()
            for(day in TimetableWithDates)
            {
                day.date
                var applications = listOf<Application?>(
                    day.timetable.application1?.get(0),
                    day.timetable.application2?.get(0),
                    day.timetable.application3?.get(0),
                    day.timetable.application4?.get(0),
                    day.timetable.application5?.get(0),
                    day.timetable.application6?.get(0),
                    day.timetable.application7?.get(0),
                )

                for (i in applications.indices){
                    if(applications[i] != null){
                        out.add(ApplicationWithDateStatus(
                            applications[i]!!.application_id,
                            applications[i]!!.classroom_id,
                            applications[i]!!.name,
                            applications[i]!!.description,
                            applications[i]!!.building,
                            applications[i]!!.classNumber,
                            day.date,
                            applications[i]!!.status,
                            i + 1))
                    }
                }
            }
            return out.toList()
        }
    }

}

class TimetableWithDate(@SerializedName("timetable") val timetable: Timetable,
                        @SerializedName("date") val date: String){

}


class Application(@SerializedName("application_id") val application_id: String,
                  @SerializedName("classroom_id") val classroom_id: String,
                  @SerializedName("name") val name: String,
                  @SerializedName("description") val description: String,
                  @SerializedName("buildings") val building: Int,
                  @SerializedName("class_number") val classNumber: Int,
                  @SerializedName("status") val status: Int){

}