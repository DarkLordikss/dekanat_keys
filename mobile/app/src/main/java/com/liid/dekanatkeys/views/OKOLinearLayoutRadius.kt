package com.liid.dekanatkeys.views

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.widget.LinearLayout

class OKOLinearLayoutRadius(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {
    private var radius = 20f

    init {
        orientation = LinearLayout.VERTICAL
        background = GradientDrawable().apply {
            cornerRadius = radius
            setColor(Color.argb(255,255,0,0))
        }
//        setBackgroundColor(Color.argb(255,255,0,0))
    }
}