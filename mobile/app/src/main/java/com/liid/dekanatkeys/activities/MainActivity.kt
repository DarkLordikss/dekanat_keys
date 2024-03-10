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
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocketListener
import retrofit2.Call

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var socketClient: OkHttpClient
    private lateinit var socketRequest: Request
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.chooseBuildingFragment, R.id.navigation_my_application, R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        socketClient = OkHttpClient.Builder().build()

//        webSocket.send("applicationId:usersenderId:True")
//        webSocket.send("applicationId:usersenderId:True")

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
                    editor.putString(getString(R.string.userId), userProfile.id)
                    editor.apply()
                    Log(userProfile.id.toString())
                    socketRequest = Request.Builder().url("ws://89.23.106.97:3223/notifications/ws/${userProfile.id}").build()
                    val webSocket = socketClient.newWebSocket(socketRequest, object : WebSocketListener(){
                        override fun onOpen(webSocket: okhttp3.WebSocket, response: Response) {
                            super.onOpen(webSocket, response)
                            Log("onOpen")
                        }

                        override fun onMessage(webSocket: okhttp3.WebSocket, text: String) {
                            super.onMessage(webSocket, text)
                            Log("onMessage")
                            Log(text)
                        }

                        override fun onClosed(webSocket: okhttp3.WebSocket, code: Int, reason: String) {
                            super.onClosed(webSocket, code, reason)
                            Log("onClosed")
                        }

                        override fun onFailure(
                            webSocket: okhttp3.WebSocket,
                            t: Throwable,
                            response: Response?
                        ) {
                            super.onFailure(webSocket, t, response)
                            Log("onFailure")
                        }
                    })

                    webSocket.send("5b1e17e1-4fd9-4fac-9718-0f53a8b4836d:29a18f80-f13b-48a1-9ab9-a95698e8816b:True")
//                    webSocket.send("application_id:user_sender_id:True")


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