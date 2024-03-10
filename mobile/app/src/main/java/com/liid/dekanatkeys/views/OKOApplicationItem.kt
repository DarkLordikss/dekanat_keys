package com.liid.dekanatkeys.views

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import com.liid.dekanatkeys.R
import com.liid.dekanatkeys.views.sheduleItem.OKOSheduleItemBooked
import java.time.LocalDate


open class OKOApplicationItem (context: Context, attrs: AttributeSet?) : OKOSheduleItemBooked(context, attrs) {

    private val statusTextView : TextView
    private val dateTextView : TextView

    init{
        statusTextView = TextView(context).apply {
            setTextColor(context.getColor(R.color.blue))
            typeface = context.resources.getFont(R.font.gotham_bold)
            textSize = 17f
        }
        dateTextView = TextView(context).apply {
            setTextColor(context.getColor(R.color.white))
            textSize = 17f
        }
        addView(dateTextView)
        addView(statusTextView)
        setBackgroundColor(context.getColor(R.color.grey))
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
        dateTextView.text = date.toString()
        statusTextView.text = status.toString()
    }
}