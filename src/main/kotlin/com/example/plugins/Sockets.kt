package com.example.plugins

import com.example.connections.ChatGroup
import com.example.connections.CustomConnection
import com.example.connections.DiceGroup
import com.example.models.CurrentSessions
import com.example.models.DicePool
import com.example.models.chat.Message
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import java.time.Duration
import io.ktor.server.application.*
import io.ktor.server.routing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*
import kotlin.collections.LinkedHashSet

val json: Json = Json {
    prettyPrint = true
}

fun Application.configureSockets() {
    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }
    routing {
        val diceConnections = Collections.synchronizedSet<CustomConnection?>(LinkedHashSet())
        val chatConnections = Collections.synchronizedSet<CustomConnection?>(LinkedHashSet())
        val usersSession = mutableMapOf<String, Int>()
        webSocket("/echo") { // websocketSession
            send("Hi from server")
            //KeyGenerator.generateKey()
        }
        webSocket("/ws") { // websocketSession
            for (frame in incoming) {
                if (frame is Frame.Text) {
                    val text = frame.readText()
                    //1;1;1;1;1;1;1
                    val dices = text.split(";")
                    val dicePool: DicePool = DicePool(
                        dices[1].toInt(), dices[2].toInt(),
                        dices[3].toInt(), dices[4].toInt(),
                        dices[5].toInt(), dices[6].toInt(),
                        dices[7].toInt()
                    )

                    val diceResult = dicePool.throwDicePool()
                    println(dicePool)
                    println("RESULT : $diceResult")
                    outgoing.send(Frame.Text("DICE RESULT : $diceResult"))
                    if (text.equals("bye", ignoreCase = true)) {
                        close(CloseReason(CloseReason.Codes.NORMAL, "Client said BYE"))
                    }
                }
            }
        }

        /**
         * Endpoint para tirar dados usando el nombre de una sesión creada con tres palabras aleatorias
         */
        webSocket("/dicethrow/{sesion}/{name}") {
            println("Adding dice throw session")

            val sessionName = call.parameters["name"]

            val connection: CustomConnection = CustomConnection(this, sessionName)
            diceConnections += connection

            try {
                send("You are connected! There are ${diceConnections.count()} users here.")
                for (frame in incoming) {
                    frame as? Frame.Text ?: continue
                    val receivedText = frame.readText()
                    val dicePool: DicePool = DicePool(
                        receivedText.split(";")[0].toInt(), receivedText.split(";")[1].toInt(),
                        receivedText.split(";")[2].toInt(), receivedText.split(";")[3].toInt(),
                        receivedText.split(";")[4].toInt(), receivedText.split(";")[5].toInt(),
                        receivedText.split(";")[6].toInt()
                    )
                    val diceResult = dicePool.throwDicePool()
                    println(dicePool)
                    println("RESULT : $diceResult")
                    //outgoing.send(Frame.Text("DICE RESULT : $diceResult"))
                    println(diceConnections)
                    diceConnections.forEach {
                        if (it.name == sessionName) {
                            it.session.send("DICE RESULT : $diceResult")
                            println("Sending to ${it.name}")
                        }
                    }
                }
            } catch (e: Exception) {
                println(e.localizedMessage)
            } finally {
                println("Removing dice throw session")
                diceConnections -= connection
            }
        }

        val connections = mutableMapOf<String, DefaultWebSocketServerSession>()
        webSocket("/chat/{name}") {
            println("Adding chat session")
            val user = call.parameters["name"]
            connections[user!!] = this
            val connection: CustomConnection = CustomConnection(this, user)
            chatConnections += connection
            try {
                send("CONECTADO")
                for (frame in incoming) {
                    frame as? Frame.Text ?: continue
                    val receivedText = frame.readText()
                    if (receivedText.startsWith("/whisper")) {
                        val commandRegex = Regex("^/whisper\\s+(\\w+)\\s+(.+)$")
                        val matchResult = commandRegex.find(receivedText)

                        if (matchResult != null) {
                            println("$user susurra a ${matchResult.groupValues[1]}")
                            val destinatario = matchResult.groupValues[1]
                            val content = matchResult.groupValues[2]
                            if (connections[destinatario] == null) {
                                connections[user]?.send("USUARIO NO ENCONTRADO")
                            } else {
                                val message = Message(content, user, destinatario, 1)
                                connections[destinatario]?.send(json.encodeToString(message))
                            }
                        }

                    } else {
                        println("Received: $receivedText from ${connection.name}")
                        val message = Message(receivedText, connection.name, "all", 1)
                        chatConnections.forEach {
                            if (it.name != connection.name) {
                                it.session.send(json.encodeToString(message))
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                println(e.localizedMessage)
            } finally {
                println("Removing chat session")
                connections.remove(user)
                chatConnections -= connection
            }
        }
        val chatGroups = mutableMapOf<String, ChatGroup>()
        val diceGroups = mutableMapOf<String, DiceGroup>()
        webSocket("/dice/{group}/{user}") {
            val group = call.parameters["group"]
            val user = call.parameters["user"]

            if (group != null && user != null) {
                val diceGroup = diceGroups.getOrPut(group) { DiceGroup(group) }

                // Unirse a la sala de dados
                diceGroup.join(this, user)
                diceGroup.addUser(user)
                CurrentSessions.usersSessionDice[group] = CurrentSessions.usersSessionDice[group]?.plus(1) ?: 1
                try {
                    //send("You are connected! There are ${diceConnections.count()} users here.")
                    for (frame in incoming) {
                        frame as? Frame.Text ?: continue
                        val receivedText = frame.readText()
                        val regex = Regex("""^\d+;\d+;\d+;\d+;\d+;\d+;\d+;[01]$""")
                        if (regex.matches(receivedText)) {


                            val dicePool: DicePool = DicePool(
                                receivedText.split(";")[0].toInt(), receivedText.split(";")[1].toInt(),
                                receivedText.split(";")[2].toInt(), receivedText.split(";")[3].toInt(),
                                receivedText.split(";")[4].toInt(), receivedText.split(";")[5].toInt(),
                                receivedText.split(";")[6].toInt(), public = receivedText.split(";")[7].toInt() == 1
                            )
                            val diceResult = dicePool.throwDicePool()
                            println(dicePool)
                            println("RESULT : $diceResult")
                            //outgoing.send(Frame.Text("DICE RESULT : $diceResult"))
                            println(diceConnections)
                            diceGroup.broadcast(diceResult, user)
                        }
                    }
                } catch (e: Exception) {
                    println(e.localizedMessage)
                } finally {
                    println("Removing chat session")
                    diceGroup.leave(this)
                    CurrentSessions.usersSessionDice[group] = CurrentSessions.usersSessionDice[group]?.plus(1) ?: 1
                    if (CurrentSessions.usersSessionDice[group] == 0)
                        CurrentSessions.usersSessionDice.remove(group)
                    diceGroups.remove(group)
                }

            }
        }

        webSocket("/chat/room/{group}/{user}") {
            val group = call.parameters["group"]
            val user = call.parameters["user"]

            if (group != null && user != null) {
                val chatGroup = chatGroups.getOrPut(group) { ChatGroup(group) }

                // Unirse a la sala de chat
                chatGroup.join(this, user)
                chatGroup.addUser(user)
                CurrentSessions.usersSessionChat[group] = CurrentSessions.usersSessionChat[group]?.plus(1) ?: 1

                println("USERS : ${chatGroup.users}")
                try {
                    for (frame in incoming) {
                        frame as? Frame.Text ?: continue
                        val message = frame.readText()
                        chatGroup.broadcast(message, user)
                    }
                } catch (e: Exception) {
                    println(e.localizedMessage)
                } finally {
                    println("Removing chat session")
                    chatGroup.leave(this)
                    CurrentSessions.usersSessionChat[group] = CurrentSessions.usersSessionChat[group]?.minus(1) ?: 0
                    if (CurrentSessions.usersSessionChat[group] == 0)
                        CurrentSessions.sessions.remove(group)
                    chatGroups.remove(group)

                }

            }
        }
    }
}