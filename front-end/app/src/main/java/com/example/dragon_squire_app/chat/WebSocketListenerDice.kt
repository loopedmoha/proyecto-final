package com.example.dragon_squire_app.chat

import android.app.Activity
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.example.dragon_squire_app.chat.models.Message
import com.example.dragon_squire_app.models.DicePool
import com.example.dragon_squire_app.models.dto.DiceDTO
import com.example.dragon_squire_app.models.dto.toDicePool
import com.example.dragon_squire_app.utils.SessionData
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener;

class WebSocketListenerDice(private val activity : Activity) : WebSocketListener() {

    val json = Json { ignoreUnknownKeys = true }
    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        SessionData.wsChat = webSocket
        Log.d("ws","Connected!")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        val message = json.decodeFromString<DiceDTO>(text)
        val user = message.user

        SessionData.dice.postValue(message.toDicePool())
        Log.d("ws", "${ SessionData.messages }")
        Log.d("ws","Recibiendo mensaje: $text")
        //Looper.prepare()
        activity.runOnUiThread {
            Toast.makeText(activity, "$user ha sacado un: ${message.result}", Toast.LENGTH_LONG).show()
        }
        //Toast.makeText(null, "$user ha sacado un: ${message.result}", Toast.LENGTH_LONG).show()
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosing(webSocket, code, reason)
        webSocket.close(1000, null)
        Log.d("ws","closing connection")
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
        t.printStackTrace()
        Log.d("ws","Error : " + t.message)
    }

    fun send(webSocket: WebSocket, message: Message){
        val json = Json { ignoreUnknownKeys = true }
        val text = json.encodeToString(message)
        webSocket.send(text)

    }
}