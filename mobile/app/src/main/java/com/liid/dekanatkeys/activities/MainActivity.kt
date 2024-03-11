package com.liid.dekanatkeys.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import com.liid.dekanatkeys.R
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.gson.Gson
import com.liid.dekanatkeys.databinding.ActivityMainBinding
import com.liid.dekanatkeys.helpers.Log
import com.liid.dekanatkeys.helpers.OKOApiSingleton
import com.liid.dekanatkeys.helpers.OKOCallback
import com.liid.dekanatkeys.helpers.WebSocketSingleton
import com.liid.dekanatkeys.models.TransferKeySocketMessage
import com.liid.dekanatkeys.models.user.UserProfile
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import retrofit2.Call

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var socketClient: OkHttpClient
    private lateinit var socketRequest: Request
    private lateinit var webSocket:WebSocket
    private lateinit var preferences: SharedPreferences
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
        preferences = getSharedPreferences(getString(R.string.SP_name), MODE_PRIVATE)

        getUserProfile()
    }
    private fun getUserProfile() {
        val token = preferences.getString(getString(R.string.jwtTokenName), null)
        OKOApiSingleton.api.fetchUserProfile("bearer $token").enqueue(OKOCallback<UserProfile>(
            successCallback = {response ->
                val userProfile = response.body()
                if (userProfile != null)
                {
                    val editor = preferences.edit()
                    editor.putString(getString(R.string.roleName), userProfile.role)
                    editor.putString(getString(R.string.userId), userProfile.id)
                    editor.putString("name", userProfile.name)
                    editor.putString("email", userProfile.email)
                    editor.putString(getString(R.string.transfer_key_socket_message), null)

                    editor.apply()

                    createWebSocket(userProfile.id)
                }
            },
            errorCallback = {response ->
                Log("error code:" + response.code().toString())
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intent)
            }
        ))
    }

    private fun createWebSocket(userId: String){
        socketRequest = Request.Builder().url("ws://89.23.106.97:3223/notifications/ws/${userId}").build()
        webSocket = socketClient.newWebSocket(socketRequest, object : WebSocketListener(){
            override fun onOpen(webSocket: okhttp3.WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                Log("onOpen")
            }

            override fun onMessage(webSocket: okhttp3.WebSocket, text: String) {
                super.onMessage(webSocket, text)
                Log("onMessage")
                Log(text)

                val exitingMessages = preferences.getString(getString(R.string.transfer_key_socket_message), null)
//                exitingMessages = null
                val editor = preferences.edit()

                if (exitingMessages == null){
                    editor.putString(getString(R.string.transfer_key_socket_message), text)
                    editor.apply()
                }
                else{
                    val keyMessagesSplitText = exitingMessages.split('#')
                    var alredyExiting = false
                    for (k in keyMessagesSplitText){
                        if (k == text) alredyExiting = true
                    }
                    if (!alredyExiting)
                    {
                        editor.putString(getString(R.string.transfer_key_socket_message),"$exitingMessages#$text")
                        editor.apply()
                    }
                }

//                send()
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

        WebSocketSingleton.initialize(webSocket)
    }

    private fun send(){
        val text = preferences.getString(getString(R.string.transfer_key_socket_message), null)
        val transferKeySocketMessage = Gson().fromJson(text, TransferKeySocketMessage::class.java)
        Log("${transferKeySocketMessage.application_id}:${transferKeySocketMessage.user_sender_id}:True")
        WebSocketSingleton.socket.send("${transferKeySocketMessage.application_id}:${transferKeySocketMessage.user_sender_id}:True")
    }

    override fun onSupportNavigateUp(): Boolean {
        val controller = findNavController(R.id.nav_host_fragment_activity_main)
        return controller.navigateUp() || super.onSupportNavigateUp()
    }



}