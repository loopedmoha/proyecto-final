package com.example.connections

import com.example.utils.KeyGenerator
import io.ktor.websocket.*
import java.util.concurrent.atomic.AtomicInteger

class CustomConnection(val session: DefaultWebSocketSession, private val sessionName : String? = null) {

    companion object {
        val lastId = AtomicInteger(0)
    }

    val name = sessionName ?: KeyGenerator.generateKey()
}