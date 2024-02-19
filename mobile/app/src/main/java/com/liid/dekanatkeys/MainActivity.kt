package com.liid.dekanatkeys

import android.os.Bundle
import com.liid.dekanatkeys.databinding.ActivityMainBinding
import com.liid.dekanatkeys.helpers.Log
import com.liid.dekanatkeys.helpers.OKOCallback
import com.liid.dekanatkeys.models.user.UserProfile
import retrofit2.Call

class MainActivity : AppCompatActivityOKOApi() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getUserProfile();
    }

    private fun getUserProfile() {
        val preferences = getSharedPreferences(getString(R.string.SP_name), MODE_PRIVATE)
        val token = preferences.getString(getString(R.string.jwtTokenName), null)
        val call: Call<UserProfile> = okoapi.fetchUserProfile("bearer $token")

        call.enqueue(OKOCallback<UserProfile>(
            successCallback = {response ->
                val userProfile = response.body()
                if (userProfile != null)
                {
                    binding.name.text = userProfile.name
                    binding.email.text = userProfile.email
                    binding.role.text = userProfile.role
                }
            },
            errorCallback = {response ->
                Log("error code:" + response.code().toString())
            }
        ))
    }
}