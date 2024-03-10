package com.liid.dekanatkeys.views.sheduleItem

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.marginBottom
import com.liid.dekanatkeys.R

open class OKOSheduleItemBooked(context: Context, attrs: AttributeSet?) : OKOSheduleItem(context, attrs) {

    private val nameTextView : TextView
    private val descriptionTextView : TextView
    private val buildingTextView : TextView
    private val classNumberTextView : TextView
    init {
        nameTextView = TextView(context).apply {
            setTextColor(context.getColor(R.color.white))
            typeface = context.resources.getFont(R.font.gotham_bold)
            textSize = 17f
        }
        descriptionTextView = TextView(context).apply {
            setTextColor(context.getColor(R.color.white))
            typeface = context.resources.getFont(R.font.gotham_book)
        }
        buildingTextView = TextView(context).apply {
            setTextColor(context.getColor(R.color.white))
            typeface = context.resources.getFont(R.font.gotham_book)
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT).apply {
                setMargins(0, 10, 0, 10)
            }
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

    open fun setApplicationInfo(name:String, description: String, building: Int, classNumber: Int){
        nameTextView.text = name
        descriptionTextView.text = description
        buildingTextView.text = "${building} корпус"
        classNumberTextView.text = "${classNumber} аудитория"
    }
}