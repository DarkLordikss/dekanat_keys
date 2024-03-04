package com.liid.dekanatkeys.models

import com.google.gson.annotations.SerializedName

class BuildingsResponse(@SerializedName("buildings") val buildings: List<String>) {
}