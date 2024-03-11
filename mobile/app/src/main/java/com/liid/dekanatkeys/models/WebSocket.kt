package com.liid.dekanatkeys.models

import okhttp3.WebSocketListener

interface WebSocket {

    public fun connect(listener: WebSocketListener)

}