package com.liid.dekanatkeys.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.liid.dekanatkeys.R
import com.liid.dekanatkeys.databinding.ActivityRegistrationBinding
import com.liid.dekanatkeys.helpers.Log
import com.liid.dekanatkeys.helpers.OKOApiSingleton
import com.liid.dekanatkeys.helpers.OKOCallback
import com.liid.dekanatkeys.models.user.RegistrationRequest
import com.liid.dekanatkeys.models.user.RegistrationResponse

class RegistrationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrationBinding
    private lateinit var email: String
    private var emailError = true
    private lateinit var password: String
    private var passwordError = true
    private lateinit var name: String
    private var nameError = true

    private lateinit var emailErrorTextView : TextView
    private lateinit var passwordErrorTextView : TextView
    private lateinit var nameErrorTextView : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
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
        nameErrorTextView = TextView(this).apply {
            setTextColor(getColor(R.color.red))
            text = getString(R.string.name_error_message)
            textSize = 15F
        }

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

        binding.name.addTextChangedListener{ text ->
            testName(text.toString())
            name = text.toString()
            checkRegistrationButtonStatus()
        }

        binding.registrationButton.setOnClickListener{
            register(email, password, name)
        }

        binding.loginButton.setOnClickListener{
            val intent = Intent(this@RegistrationActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun register(email:String, password:String, name: String){
        val registrationRequest = RegistrationRequest(email, password, name)

        OKOApiSingleton.api.registrationUser(registrationRequest).enqueue(OKOCallback<RegistrationResponse>(
            successCallback = {response ->
                val intent = Intent(this@RegistrationActivity, SuccessRegistrationActivity::class.java)
                startActivity(intent)
            },
            errorCallback = {response ->
                Log("error code:" + response.code().toString())
                if(response.code() == 400){
                    Toast.makeText(this, "Пользователь уже существует", Toast.LENGTH_LONG).show()
                }
            }
        ))
    }

    private fun checkRegistrationButtonStatus(){
        binding.registrationButton.isEnabled = !emailError && !passwordError && !nameError
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
//        val passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*?&]{6,}$".toRegex()
//        setPasswordError(!password.matches(passwordRegex))
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

    private fun testName(name:String) {
        setNameError(name.length !in 3..30)
    }
    private fun setNameError(error:Boolean) {
        if (error) {
            nameError = true
            binding.name.setErrorColor()
            addChild(binding.nameLayout, nameErrorTextView)
        } else {
            nameError = false
            binding.name.setColor()
            removeChild(binding.nameLayout, nameErrorTextView)
        }
    }
}