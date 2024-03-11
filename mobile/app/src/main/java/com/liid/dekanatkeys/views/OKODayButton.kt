package com.liid.dekanatkeys.views

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.widget.LinearLayout
import com.liid.dekanatkeys.R
import java.time.LocalDate


class OKODayButton(context: Context, attrs: AttributeSet?) : androidx.appcompat.widget.AppCompatTextView(context, attrs) {
    private val radius = 10f
    private val size = 35
    private lateinit var date: LocalDate

    init {
        val dip = convertToDp(size)
        layoutParams = LinearLayout.LayoutParams(dip, dip)
        gravity = Gravity.CENTER
        textSize = 17f
        typeface = context.resources.getFont(R.font.gotham_bold)
        setInactive()
    }

    fun setDate(date: LocalDate){
        this.date = date
        text = date.dayOfMonth.toString()
    }

    fun getDate(): LocalDate { return date}

    fun setActive(){
        background = GradientDrawable().apply {
            cornerRadius = radius
            setColor(context.getColor(R.color.blue))
        }
        setTextColor(context.getColor(R.color.white))
    }

    fun setInactive(){
        background = GradientDrawable().apply {
            cornerRadius = radius
            setColor(Color.argb(0,0,0,0))
        }
        setTextColor(context.getColor(R.color.black))
    }

    private fun convertToDp(a: Int) : Int {
        val px = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            a.toFloat(),
            resources.displayMetrics
        )
        return px.toInt()
    }
}