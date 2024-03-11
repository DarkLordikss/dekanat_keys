package com.liid.dekanatkeys.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.liid.dekanatkeys.databinding.ActivitySeccessRegistrationBinding

class SuccessRegistrationActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySeccessRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeccessRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener{
            val intent = Intent(this@SuccessRegistrationActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}