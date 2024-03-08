package com.liid.dekanatkeys.views.sheduleItem

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.liid.dekanatkeys.R

open class OKOSheduleItem (context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {
    private val padding = 30
    private val radius = 20f
    private var lessonNumber = 0

    private val lessonTextView: TextView

    init {

        lessonTextView = TextView(context).apply {
            text = "0"
            setTextColor(context.getColor(R.color.white))
            typeface = context.resources.getFont(R.font.gotham_book)
        }
        super.addView(lessonTextView)

        layoutParams = LinearLayout.LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        ).apply {
            bottomMargin = 10
            setPadding(padding,padding,padding,padding)
        }
        orientation = VERTICAL
        background = GradientDrawable().apply {
            cornerRadius = radius
            setColor(context.getColor(R.color.black))
        }
    }

    override fun setBackgroundColor(color: Int) {
        background = GradientDrawable().apply {
            cornerRadius = radius
            setColor(color)
        }
    }

    override fun addView(child: View?) {
        super.addView(child, childCount-1)
    }

    fun setLessonNumber(number:Int){
        lessonNumber = number
        lessonTextView.text = lessonNumber.toString() + "-ая пара"
    }

}