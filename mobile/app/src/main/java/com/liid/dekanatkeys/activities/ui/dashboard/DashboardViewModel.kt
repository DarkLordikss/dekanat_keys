package com.liid.dekanatkeys.activities.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.liid.dekanatkeys.models.TimetableWithList
import java.time.LocalDate

class DashboardViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text

    var building: String? = null
    var classroom: String? = null
    var startDate: LocalDate? = null
    var endDate: LocalDate? = null

    var timetables = mutableListOf<TimetableWithList>()
    var currentDay = 0
    var weekOffset = -3L

}