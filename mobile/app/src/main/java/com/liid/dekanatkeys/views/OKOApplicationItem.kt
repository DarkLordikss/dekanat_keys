package com.liid.dekanatkeys.views

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.liid.dekanatkeys.R
import com.liid.dekanatkeys.views.sheduleItem.OKOSheduleItemBooked
import java.time.LocalDate
import java.time.format.DateTimeFormatter


open class OKOApplicationItem (context: Context, attrs: AttributeSet?) : OKOSheduleItemBooked(context, attrs) {

    var applicationIdEx : String? = null
    private val statusTextView : TextView
    private val dateTextView : TextView
    val changeButton : Button

    init{
        statusTextView = TextView(context).apply {
            setTextColor(context.getColor(R.color.white))
            typeface = context.resources.getFont(R.font.gotham_bold)
            textSize = 17f
            layoutParams = LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 10, 0, 10)
            }

        }
        dateTextView = TextView(context).apply {
            setTextColor(context.getColor(R.color.white))
            typeface = context.resources.getFont(R.font.gotham_book)
            layoutParams = LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 10, 0, 10)
            }
        }
        changeButton = Button(context).apply {
            setText("Передать")
            setTextColor(context.getColor(R.color.white))
            setBackgroundColor(context.getColor(R.color.blue))
            layoutParams = LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
            ).apply {
                RelativeLayout.ALIGN_PARENT_TOP
                RelativeLayout.ALIGN_PARENT_RIGHT
            }

        }

        addView(dateTextView)
        addView(statusTextView)
        addView(changeButton)
        setBackgroundColor(context.getColor(R.color.green))
    }

    fun setApplicationInfo(
        name: String,
        description: String,
        building: Int,
        classNumber: Int,
        date:LocalDate,
        status: Int
    ) {
        super.setApplicationInfo(name, description, building, classNumber)

//        applicationIdEx = applicationId
        var formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        var formateDate = date.format(formatter)

        dateTextView.text = formateDate

        val statusText = when (status){
            1 -> "Не обработано"
            2 -> "Подтверждено"
            3 -> "Ключ получен"
            4 -> "Ключ сдан"
            5 -> "Отклонено"
            else -> "Недействительно"
        }

        statusTextView.text = "Статус: ${statusText}"
    }
}