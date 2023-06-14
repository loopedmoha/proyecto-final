package com.example.dragon_squire_app.chat

import android.util.Log
import com.example.dragon_squire_app.chat.models.Message
import com.example.dragon_squire_app.utils.SessionData
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener;

class WebSocketListenerChat : WebSocketListener() {

   val json = Json { ignoreUnknownKeys = true }
   override fun onOpen(webSocket: WebSocket, response: Response) {
      super.onOpen(webSocket, response)
      SessionData.wsChat = webSocket
      Log.d("ws","Connected!")
   }

   override fun onMessage(webSocket: WebSocket, text: String) {
      val message = json.decodeFromString<Message>(text)

      SessionData.prueba.postValue(message)
      SessionData.messages.add(message)
      Log.d("ws", "${ SessionData.messages }")
      Log.d("ws","Recibiendo mensaje: $text")
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