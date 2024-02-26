package com.liid.dekanatkeys.helpers

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocketListener

class WebSocketClient {
    init {
        val client = OkHttpClient.Builder().build()
        val request = Request.Builder().url("ws://26.225.154.74:8000/notifications/ws/7cf611f3-e752-4b10-af2c-73e15aadc2e1").build()
        val webSocket = client.newWebSocket(request, object : WebSocketListener(){
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
        webSocket.send("a595e12a-86b9-4715-9eac-4041606caee3:7cf611f3-e752-4b10-af2c-73e15aadc2e1:7c09b8f7-6b47-4ec2-8434-530a175f050b:True")
    }
}