package com.example.connections

import com.example.models.chat.Message
import com.example.plugins.json
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.serialization.encodeToString

class ChatGroup(private val name: String) {
    private val sessions = mutableListOf<DefaultWebSocketServerSession>()
    val users = mutableListOf<String>()
    private val sessionsW = mutableMapOf<String, DefaultWebSocketServerSession>()
    suspend fun join(session: DefaultWebSocketServerSession, user: String) {
        sessions.add(session)
        sessionsW[user] = session
        //session.send("Bienvenido a: $name")
    }

    suspend fun leave(session: DefaultWebSocketServerSession) {
        sessions.remove(session)
        session.send("Adios")
    }

    fun addUser(user: String) {
        users.add(user)
    }

    suspend fun broadcast(message: String, user: String) {
        if (!message.startsWith("/whisper")) {
            sessions.forEach {
                val messageEncoded = Message(message, user,"all", 1)
                it.send(json.encodeToString(messageEncoded))
            }
        } else {
            val commandRegex = Regex("^/whisper\\s+(\\w+)\\s+(.+)$")
            val matchResult = commandRegex.find(message)

            if (matchResult != null) {
                val recipient = matchResult.groupValues[1]
                val content = matchResult.groupValues[2]
                val messageEncoded = Message(content, user, recipient, 1)

                if (sessionsW[recipient] == null) {
                    sessionsW[user]?.send("USUARIO NO ENCONTRADO")
                } else
                    sessionsW[recipient]?.send(json.encodeToString(messageEncoded))
            }
        }

    }
}