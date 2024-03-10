package com.liid.dekanatkeys.views

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import com.liid.dekanatkeys.R

class UserInfoItem(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {
    private val padding = 30
    private val radius = 20f

    private val email: TextView
    private val name: TextView
    var userId: String = ""

    init{
        email = TextView(context).apply {
            setTextColor(context.getColor(R.color.white))
            typeface = context.resources.getFont(R.font.gotham_bold)
        }
        name = TextView(context).apply {
            setTextColor(context.getColor(R.color.white))
            typeface = context.resources.getFont(R.font.gotham_book)
        }
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
            setColor(context.getColor(R.color.blue))
        }
        addView(email)
        addView(name)
    }

    fun setUserInfo(email:String, name:String, id:String){
        this.email.text = email
        this.name.text = name
        this.userId = id
    }

    override fun setBackgroundColor(color: Int) {
        background = GradientDrawable().apply {
            cornerRadius = radius
            setColor(color)
        }
    }
}