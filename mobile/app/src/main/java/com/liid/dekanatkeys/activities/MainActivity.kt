package com.liid.dekanatkeys.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.liid.dekanatkeys.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        getUserProfile();
    }

//    private fun getUserProfile() {
//        val preferences = getSharedPreferences(getString(R.string.SP_name), MODE_PRIVATE)
//        val token = preferences.getString(getString(R.string.jwtTokenName), null)
//        Log(if (token != null) "get$ token" else "token not found")
//        val call: Call<UserProfile> = OKOApiSingleton.api.fetchUserProfile("bearer $token")
//
//        call.enqueue(OKOCallback<UserProfile>(
//            successCallback = {response ->
//                val userProfile = response.body()
//                if (userProfile != null)
//                {
//                    binding.name.text = userProfile.name
//                    binding.email.text = userProfile.email
//                    binding.role.text = userProfile.role
//                }
//            },
//            errorCallback = {response ->
//                Log("error code:" + response.code().toString())
//            }
//        ))
//    }
}