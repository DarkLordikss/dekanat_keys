package com.liid.dekanatkeys.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.liid.dekanatkeys.R
import com.liid.dekanatkeys.activities.ui.dashboard.DashboardFragment
import com.liid.dekanatkeys.activities.ui.dashboard.DashboardViewModel
import com.liid.dekanatkeys.helpers.Log
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@SuppressLint("UseCompatLoadingForDrawables")
class OKODateBar(context: Context, attrs: AttributeSet?, private val parentFragment: OKODateBarInteraction) : LinearLayout(context, attrs) {

    private val dayButtons : List<OKODayButton>
    private val dayButtonLayouts : List<LinearLayout>
    private val nextImageButton : ImageButton
    private val prevImageButton : ImageButton
    private val weekDays = listOf("пн", "вт" , "ср", "чт", "пт", "сб", "вс")
    private val mouthNames = listOf("Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь")
    private lateinit var activeButton: OKODayButton

    lateinit var startDate: LocalDate
    lateinit var endDate: LocalDate
    var activeButtonPos = 0

    private val dashboardViewModel: DashboardViewModel = (parentFragment as DashboardFragment).dashboardViewModel

    init {
//        parentFragment = context as OKODateBarInteraction
        dayButtons = List(7) { index ->
            val button = OKODayButton(context, null)
            button.setOnClickListener{it ->
                val btn = it as OKODayButton
                setActive(btn)
                parentFragment.dateButtonClicked(activeButtonPos)
            }
            button
        }
        setDatesToButtons()

        dayButtonLayouts = List(dayButtons.size){index ->
            val l = LinearLayout(context)
            l.orientation = VERTICAL
            l.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f)
            l.gravity = Gravity.CENTER

            val weekDayTextView = TextView(context).apply {
                gravity = Gravity.CENTER
                text = weekDays[index]
                setTextColor(context.getColor(R.color.gray))
            }

            l.addView(weekDayTextView)
            l.addView(dayButtons[index])
            l
        }

        for (l in dayButtonLayouts){
            addView(l)
        }

        nextImageButton = ImageButton(context).apply {
            setImageDrawable(context.getDrawable(R.drawable.arrow_next))
            setPadding(left + 5, top, right + 5, bottom)
            layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
            setBackgroundColor(Color.argb(0,0,0,0))
            scaleX = 1.5f
            scaleY = 1.5f
            setOnClickListener{
                dashboardViewModel.weekOffset++
                setDatesToButtons()
                parentFragment.setStartEndDates(startDate, endDate, activeButtonPos)
            }
        }
        prevImageButton = ImageButton(context).apply {
            setImageDrawable(context.getDrawable(R.drawable.arrow_prev))
            setPadding(left + 5, top, right + 5, bottom)
            layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
            setBackgroundColor(Color.argb(0,0,0,0))
            scaleX = 1.5f
            scaleY = 1.5f
            setOnClickListener{
                dashboardViewModel.weekOffset--
                setDatesToButtons()
                parentFragment.setStartEndDates(startDate, endDate, activeButtonPos)
            }
        }

        addView(nextImageButton)
        addView(prevImageButton, 0)

        orientation = HORIZONTAL
        layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        )
    }

    private fun setDatesToButtons(){
        val dates = getDaysOfWeek(dashboardViewModel.weekOffset)

        startDate = dates[0]
        endDate =  dates[dates.size-1]
        val currDate = LocalDate.now()

        for (i in 0 until dates.size){
            dayButtons[i].setDate(dates[i])
        }

        setActive(dayButtons[0])
        for (i in 0 until dates.size){
            if(dates[i] == currDate){
                setActive(dayButtons[i])
            }
        }
    }

    private fun setActive(btn: OKODayButton){
        for (b in dayButtons){
            b.setInactive()
        }
        btn.setActive()
        activeButton = btn
        activeButtonPos = dayButtons.indexOf(activeButton)
        parentFragment.setMonth(mouthNames[activeButton.getDate().monthValue-1])
    }

    private fun getDaysOfWeek(offset: Long) : MutableList<LocalDate>{
        val dates = mutableListOf<LocalDate>()
        val currentDay = LocalDate.now().plusDays(offset * 7)

        val currentDayOfWeek = currentDay.dayOfWeek.ordinal
        val mondayIndex = - currentDayOfWeek
        var di = mondayIndex.toLong()
        for (i in 0..6 ){
            dates.add(currentDay.plusDays(di))
            di++
        }
        return dates
    }
}