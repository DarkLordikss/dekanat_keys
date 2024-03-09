package com.liid.dekanatkeys.views.sheduleItem

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import com.liid.dekanatkeys.R

class OKOSheduleItemBooked(context: Context, attrs: AttributeSet?) : OKOSheduleItem(context, attrs) {

    private val nameTextView : TextView
    private val descriptionTextView : TextView
    private val buildingTextView : TextView
    private val classNumberTextView : TextView
    init {
        nameTextView = TextView(context).apply {
            setTextColor(context.getColor(R.color.white))
            typeface = context.resources.getFont(R.font.gotham_bold)
        }
        descriptionTextView = TextView(context).apply {
            setTextColor(context.getColor(R.color.white))
            typeface = context.resources.getFont(R.font.gotham_book)
        }
        buildingTextView = TextView(context).apply {
            setTextColor(context.getColor(R.color.white))
            typeface = context.resources.getFont(R.font.gotham_book)
        }
        classNumberTextView = TextView(context).apply {
            setTextColor(context.getColor(R.color.white))
            typeface = context.resources.getFont(R.font.gotham_book)
        }
        addView(nameTextView)
        addView(descriptionTextView)
        addView(buildingTextView)
        addView(classNumberTextView)
        setBackgroundColor(context.getColor(R.color.grey))
    }

    fun setApplicationInfo(name:String, description: String, building: Int, classNumber: Int){
        nameTextView.text = name
        descriptionTextView.text = "рыбалка рыбалка рыбалка рыбалка рыбалка рыбалка рыбалка рыбалка рыбалка рыбалка рыбалка рыбалка рыбалка рыбалка рыбалка рыбалка рыбалка рыбалка рыбалка рыбалка рыбалка рыбалка рыбалка рыбалка рыбалка рыбалка рыбалка рыбалка рыбалка рыбалка"
        buildingTextView.text = "${building} корпус"
        classNumberTextView.text = "${classNumber} аудитория"
    }
}