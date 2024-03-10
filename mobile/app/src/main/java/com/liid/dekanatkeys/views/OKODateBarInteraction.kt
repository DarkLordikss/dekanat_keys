package com.liid.dekanatkeys.views

import java.time.LocalDate

interface OKODateBarInteraction {
    fun setMonth(month: String)

    fun setStartEndDates(start: LocalDate, end: LocalDate)

    fun dateButtonClicked(pos: Int)
}