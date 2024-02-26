package com.liid.dekanatkeys.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.BoringLayout
import android.view.View
import androidx.core.widget.addTextChangedListener
import com.liid.dekanatkeys.R
import com.liid.dekanatkeys.databinding.ActivityRegistrationBinding
import com.liid.dekanatkeys.helpers.Log

class RegistrationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrationBinding
    private lateinit var email: String
    private var emailError = true
    private lateinit var password: String
    private var passwordError = true
    private lateinit var name: String
    private var nameError = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

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


        binding.loginButton.setOnClickListener{
            val intent = Intent(this@RegistrationActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun checkRegistrationButtonStatus(){
        Log((!emailError && !passwordError && !nameError).toString())
        binding.registrationButton.isEnabled = !emailError && !passwordError && !nameError
    }

    private fun testEmail(email:String) {
        val emailRegex = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$".toRegex()
        setEmailError(!email.matches(emailRegex))
    }

    private fun setEmailError(error:Boolean) {
        if (error) {
            emailError = true
            binding.emailErrorView.visibility = View.VISIBLE
            binding.email.setErrorColor()
        } else {
            emailError = false
            binding.emailErrorView.visibility = View.INVISIBLE
            binding.email.setColor()
        }
    }

    private fun testPassword(password:String) {
        val passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*?&]{6,}$".toRegex()
        setPasswordError(!password.matches(passwordRegex))
    }

    private fun setPasswordError(error:Boolean) {
        if (error) {
            passwordError = true
            binding.passwordErrorView.visibility = View.VISIBLE
            binding.password.setErrorColor()
        } else {
            passwordError = false
            binding.passwordErrorView.visibility = View.INVISIBLE
            binding.password.setColor()
        }
    }

    private fun testName(name:String) {
        setNameError(name.length !in 3..30)
    }

    private fun setNameError(error:Boolean) {
        if (error) {
            nameError = true
            binding.nameErrorView.visibility = View.VISIBLE
            binding.name.setErrorColor()
        } else {
            nameError = false
            binding.nameErrorView.visibility = View.INVISIBLE
            binding.name.setColor()
        }
    }
}