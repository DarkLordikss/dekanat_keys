package com.liid.dekanatkeys.activities

import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import com.liid.dekanatkeys.R
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.liid.dekanatkeys.databinding.ActivityMainBinding
import com.liid.dekanatkeys.helpers.Log
import com.liid.dekanatkeys.helpers.OKOApiSingleton
import com.liid.dekanatkeys.helpers.OKOCallback
import com.liid.dekanatkeys.models.user.UserProfile
import retrofit2.Call

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.chooseBuildingFragment, R.id.navigation_my_application, R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        getUserProfile()
    }
    private fun getUserProfile() {
        val preferences = getSharedPreferences(getString(R.string.SP_name), MODE_PRIVATE)
        val token = preferences.getString(getString(R.string.jwtTokenName), null)
        Log(if (token != null) "get$ token" else "token not found")
        OKOApiSingleton.api.fetchUserProfile("bearer $token").enqueue(OKOCallback<UserProfile>(
            successCallback = {response ->
                val userProfile = response.body()
                if (userProfile != null)
                {
                    val editor = preferences.edit()
                    editor.putString(getString(R.string.roleName), userProfile.role)
                    editor.apply()
                }
            },
            errorCallback = {response ->
                Log("error code:" + response.code().toString())
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intent)
            }
        ))
    }

}