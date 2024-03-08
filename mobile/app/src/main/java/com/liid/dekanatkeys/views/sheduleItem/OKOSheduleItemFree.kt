package com.liid.dekanatkeys.views.sheduleItem

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import com.liid.dekanatkeys.R

class OKOSheduleItemFree (context: Context, attrs: AttributeSet?) : OKOSheduleItem(context, attrs) {

    private val freeTextView: TextView

    init {
        freeTextView = TextView(context).apply {
            text = "Свободно"
            setTextColor(context.getColor(R.color.white))
            typeface = context.resources.getFont(R.font.gotham_book)
        }

        addView(freeTextView)
        setBackgroundColor(context.getColor(R.color.blue))
    }
}