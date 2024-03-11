package com.liid.dekanatkeys.activities.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.liid.dekanatkeys.models.TimetableWithList
import java.time.LocalDate

class DashboardViewModel : ViewModel() {


    var building: String? = null
    var classroom: String? = null


    var timetables = mutableListOf<TimetableWithList>()
    var currentDayPos = 0
    var weekOffset = 0L

    var classroomId: String? = null
    var name : String? = null
    var description : String? = null
    var currentDate: LocalDate = LocalDate.now()
    var lessonNumber = 0
    var dublicates = 1
}