package com.liid.dekanatkeys.activities.ui.myApplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyApplicationViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is my application Fragment"
    }
    val text: LiveData<String> = _text
}