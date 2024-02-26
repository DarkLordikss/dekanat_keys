package com.liid.dekanatkeys.views

import android.content.Context
import android.graphics.Paint
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import com.liid.dekanatkeys.R

class OKOButton(private val context: Context, attrs: AttributeSet?) : androidx.appcompat.widget.AppCompatButton(context, attrs) {

    private var inited = false
    private var radius = 5f
    private val extraPadding = 16

    init {
        setTextColor(context.getColor(R.color.white))
        typeface = context.resources.getFont(R.font.gotham_medium)
        isAllCaps = false;
        inited = true;
        setPadding(extraPadding, paddingTop, extraPadding, paddingBottom)
        updateButtonState();
    }

    private fun updateButtonState() {
        val backgroundColorResId = if (isEnabled) R.color.blue else R.color.gray
        background = GradientDrawable().apply {
            cornerRadius = radius
            setColor(context.getColor(backgroundColorResId))
        }
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        if(inited) updateButtonState()
    }

    private val textPaint = Paint().apply {
        color = currentTextColor
        this.textSize = this@OKOButton.textSize
        isAntiAlias = true
        isAllCaps = false
    }
}