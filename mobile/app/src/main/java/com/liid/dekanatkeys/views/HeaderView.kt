package com.liid.dekanatkeys.views

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import com.liid.dekanatkeys.R
import com.liid.dekanatkeys.activities.LoginActivity
import com.liid.dekanatkeys.helpers.Log
import com.liid.dekanatkeys.helpers.OKOApiSingleton
import com.liid.dekanatkeys.helpers.OKOCallback
import com.liid.dekanatkeys.models.user.UserProfile
import retrofit2.Call

class HeaderView(private val context: Context, attrs: AttributeSet?) : View(context, attrs){

    private val nameTextView = TextView(context).apply {
        text = "error"
    }
    private val emailTextView = TextView(context).apply {
        text = "error"
    }
    private val roleTextView = TextView(context).apply {
        text = "error"
    }
    private val linearLayout = LinearLayout(context).apply {
        orientation = LinearLayout.VERTICAL
        layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    init {
        linearLayout.addView(nameTextView)
        linearLayout.addView(emailTextView)
        linearLayout.addView(roleTextView)

//        getUserProfile()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        // Настройка размеров HeaderView, если нужно
        // ...

        // Настройка размеров дочерних элементов
//        measureChild(nameTextView, widthMeasureSpec, heightMeasureSpec)
//        measureChild(emailTextView, widthMeasureSpec, heightMeasureSpec)
//        measureChild(roleTextView, widthMeasureSpec, heightMeasureSpec)
    }

    private fun getUserProfile() {
        val preferences = context.getSharedPreferences(context.getString(R.string.SP_name), MODE_PRIVATE)
        val token = preferences.getString(context.getString(R.string.jwtTokenName), null)

        Log(if (token != null) "get$ token" else "token not found")
        val call: Call<UserProfile> = OKOApiSingleton.api.fetchUserProfile("bearer $token")

        call.enqueue(OKOCallback<UserProfile>(
            successCallback = {response ->
                val userProfile = response.body()
                if (userProfile != null)
                {
                    nameTextView.text = userProfile.name
                    emailTextView.text = userProfile.email
                    roleTextView.text = userProfile.role
                }
                else{
                    val intent = Intent(context, LoginActivity::class.java)
                    startActivity(context, intent, null)
                }
            },
            errorCallback = {response ->
                Log("error code:" + response.code().toString())
                val intent = Intent(context, LoginActivity::class.java)
                startActivity(context, intent, null)
            }
        ))
    }

}