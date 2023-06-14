package com.example.connections

import com.example.models.DicePool
import com.example.models.toDto
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class DiceGroup(private val name: String) {
    private val sessions = mutableListOf<DefaultWebSocketServerSession>()
    private val users = mutableListOf<String>()
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
        val json = Json { prettyPrint = true }
        val dicePool = json.decodeFromString(DicePool.serializer(), message)
        val diceDto = dicePool.toDto(user)
        sessions.forEach {
            it.send(json.encodeToString(diceDto))
        }
    }
}