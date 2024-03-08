package com.liid.dekanatkeys.models

import com.google.gson.annotations.SerializedName

class ClassroomResponse(@SerializedName("classrooms") val classrooms: List<Classroom>) {
}