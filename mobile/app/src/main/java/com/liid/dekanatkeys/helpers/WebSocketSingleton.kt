package com.liid.dekanatkeys.helpers

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

object WebSocketSingleton {

    lateinit var socket: WebSocket
    fun initialize(socket: WebSocket){
        this.socket = socket
    }

}