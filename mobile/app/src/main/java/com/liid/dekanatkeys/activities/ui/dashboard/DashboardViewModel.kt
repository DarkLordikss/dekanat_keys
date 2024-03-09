package com.liid.dekanatkeys.activities.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DashboardViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text

    var building: String? = null
    var classroom: String? = null

//    private val _building = MutableLiveData<String>()
//    private val _classroom = MutableLiveData<String>()
//
//    // Public read-only LiveData properties
//    val building: LiveData<String> get() = _building
//    val classroom: LiveData<String> get() = _classroom
//
//    // Methods to update values
//    fun setBuilding(value: String) {
//        _building.value = value
//    }
//
//    fun setClassroom(value: String) {
//        _classroom.value = value
//    }
}