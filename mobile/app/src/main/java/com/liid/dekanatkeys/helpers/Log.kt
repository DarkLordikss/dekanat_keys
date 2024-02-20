package com.liid.dekanatkeys.helpers

public final class Log(private val msg:String) {
    init {
        android.util.Log.d("OKOTag", msg)
    }
}