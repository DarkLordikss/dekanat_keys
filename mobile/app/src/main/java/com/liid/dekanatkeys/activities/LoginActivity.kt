package com.liid.dekanatkeys.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.liid.dekanatkeys.R
import com.liid.dekanatkeys.databinding.ActivityLoginBinding
import com.liid.dekanatkeys.helpers.Log
import com.liid.dekanatkeys.helpers.OKOApiSingleton
import com.liid.dekanatkeys.helpers.OKOCallback
import com.liid.dekanatkeys.models.ApiService
import com.liid.dekanatkeys.models.WebSocket
import com.liid.dekanatkeys.models.user.LoginRequest
import com.liid.dekanatkeys.models.user.LoginResponse
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocketListener
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity: AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private lateinit var email: String
    private lateinit var password: String
    private var emailError = true
    private var passwordError = true
    private lateinit var emailErrorTextView : TextView
    private lateinit var passwordErrorTextView : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        OKOApiSingleton.initialize(getString(R.string.Api))
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        emailErrorTextView = TextView(this).apply {
            setTextColor(getColor(R.color.red))
            text = getString(R.string.email_error_message)
            textSize = 15F
        }
        passwordErrorTextView = TextView(this).apply {
            setTextColor(getColor(R.color.red))
            text = getString(R.string.password_error_message)
            textSize = 15F
        }

        email = binding.email.text.toString()
        password = binding.password.text.toString()

        binding.email.addTextChangedListener{ text ->
            testEmail(text.toString())
            email = text.toString()
            checkRegistrationButtonStatus()
        }

        binding.password.addTextChangedListener{ text ->
            testPassword(text.toString())
            password = text.toString()
            checkRegistrationButtonStatus()
        }

        binding.loginButton.setOnClickListener {
            login(email, password)
        }

        binding.registerButton.setOnClickListener{
            val intent = Intent(this@LoginActivity, RegistrationActivity::class.java)
            startActivity(intent)
        }
    }

    private fun login(email:String, password:String){
        val loginRequest = LoginRequest(email, password)

        val call: Call<LoginResponse> = OKOApiSingleton.api.loginUser(loginRequest)

        call.enqueue(OKOCallback<LoginResponse>(
            successCallback = {response ->
                val token = response.body()?.token
                if (token != null) {
                    Log(token)
                    saveToken(token)

                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                }
                else Log("null token")
            },
            errorCallback = {response ->
                Log("error code:" + response.code().toString())
            }
        ))
    }

    private fun saveToken(token: String) {
        val preferences = getSharedPreferences(getString(R.string.SP_name), MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString(getString(R.string.jwtTokenName), token)
        editor.apply()
        Log("token putted")
    }

    private fun checkRegistrationButtonStatus(){
        binding.loginButton.isEnabled = !emailError && !passwordError
    }

    private fun addChild(layout: LinearLayout, view: View){
        if (layout.indexOfChild(view) == -1) layout.addView(view)
    }
    private fun removeChild(layout: LinearLayout, view: View){
        if (layout.indexOfChild(view) != -1) layout.removeView(view)
    }

    private fun testEmail(email:String) {
        val emailRegex = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$".toRegex()
        setEmailError(!email.matches(emailRegex))
    }
    private fun setEmailError(error:Boolean) {
        if (error) {
            emailError = true
            binding.email.setErrorColor()
            addChild(binding.emailLayout, emailErrorTextView)
        } else {
            emailError = false
            binding.email.setColor()
            removeChild(binding.emailLayout, emailErrorTextView)
        }
    }

    private fun testPassword(password:String) {
        setPasswordError(password.length < 6)
    }

    private fun setPasswordError(error:Boolean) {
        if (error) {
            passwordError = true
            binding.password.setErrorColor()
            addChild(binding.passwordLayout, passwordErrorTextView)
        } else {
            passwordError = false
            binding.password.setColor()
            removeChild(binding.passwordLayout, passwordErrorTextView)
        }
    }
}