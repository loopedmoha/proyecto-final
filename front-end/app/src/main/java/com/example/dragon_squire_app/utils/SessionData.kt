package com.example.dragon_squire_app.utils

import androidx.lifecycle.MutableLiveData
import com.example.dragon_squire_app.chat.models.Message
import com.example.dragon_squire_app.models.DicePool
import com.example.dragon_squire_app.models.player.Player
import okhttp3.WebSocket

object SessionData{
    var token: String = ""
    var username = ""
    var sessionName = MutableLiveData<String>("")
    val users = mutableListOf<String>()
    lateinit var player : Player
    val messages = mutableListOf<Message>()
    var prueba = MutableLiveData<Message>()
    var dice = MutableLiveData<DicePool>()
    val connected = false
    lateinit var wsChat: WebSocket
    lateinit var wsDice: WebSocket
}
