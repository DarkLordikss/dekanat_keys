package com.liid.dekanatkeys.views

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.liid.dekanatkeys.R
import com.liid.dekanatkeys.views.sheduleItem.OKOSheduleItemBooked

class OKOApplicationItemMessage (context: Context, attrs: AttributeSet?) : OKOApplicationItem(context, attrs)  {

    private val buttonLayout: LinearLayout

    val applyButton : Button
    val cancelButton : Button

    init {
        buttonLayout = LinearLayout(context).apply {
            orientation = HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            ).apply {

            }
        }
        removeView(changeButton)
        applyButton = Button(context).apply {
            setText("Принять")
            setTextColor(context.getColor(R.color.white))
            setBackgroundColor(context.getColor(R.color.blue))
            layoutParams = LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
            ).apply {
                RelativeLayout.ALIGN_PARENT_TOP
                RelativeLayout.ALIGN_PARENT_RIGHT
                marginEnd = 20
            }

        }
        cancelButton = Button(context).apply {
            setText("Отклонить")
            setTextColor(context.getColor(R.color.white))
            setBackgroundColor(context.getColor(R.color.red))
            layoutParams = LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
            ).apply {
                RelativeLayout.ALIGN_PARENT_TOP
                RelativeLayout.ALIGN_PARENT_RIGHT
            }

        }
        buttonLayout.addView(applyButton)
        buttonLayout.addView(cancelButton)
        addView(buttonLayout)
    }
}