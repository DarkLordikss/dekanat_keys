package com.liid.dekanatkeys.views

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.liid.dekanatkeys.R

class OKOEditText (context: Context, attrs: AttributeSet?) : AppCompatEditText(context, attrs) {

    private var radius = 5f
    private val extraPadding = 20
    private var strokeWidth = 3
    private var strokeColor = context.getColor(R.color.gray)

    init {
        background = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = radius
            setColor(Color.argb(0,0,0,0))
            setStroke(strokeWidth, strokeColor)
        }
        setPadding(paddingLeft + extraPadding, paddingTop, paddingRight, paddingBottom)
        setTextColor(context.getColor(R.color.black))
        setHintTextColor(strokeColor)
        typeface = context.resources.getFont(R.font.gotham_book)
    }

}