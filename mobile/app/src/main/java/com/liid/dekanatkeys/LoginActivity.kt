package com.liid.dekanatkeys

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.liid.dekanatkeys.databinding.ActivityLoginBinding
import com.liid.dekanatkeys.helpers.Log
import com.liid.dekanatkeys.helpers.OKOCallback
import com.liid.dekanatkeys.models.LoginRequest
import com.liid.dekanatkeys.models.LoginResponse
import retrofit2.Call

class LoginActivity: AppCompatActivityOKOApi() {
    private lateinit var binding: ActivityLoginBinding

    private lateinit var email: String
    private lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        email = binding.email.text.toString()
        password = binding.password.text.toString()

        binding.email.addTextChangedListener{ text ->
            email = text.toString()
        }

        binding.password.addTextChangedListener{ text ->
            password = text.toString()
        }

        binding.loginButton.setOnClickListener {
            Log("$email $password")
            login(email, password)

            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun login(email:String, password:String){
        val loginRequest = LoginRequest(email, password)

        val call: Call<LoginResponse> = okoapi.loginUser(loginRequest)

        call.enqueue(OKOCallback<LoginResponse>(
            successCallback = {response ->
                val token = response.body()?.token
                if (token != null) {
                    Log(token)
                    saveToken(token)
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
    }
}