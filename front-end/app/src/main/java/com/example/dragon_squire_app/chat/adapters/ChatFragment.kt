package com.example.dragon_squire_app.chat.adapters

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dragon_squire_app.chat.WebSocketListenerChat
import com.example.dragon_squire_app.chat.models.Message
import com.example.dragon_squire_app.utils.SessionData
import com.example.dragon_squire_app.databinding.FragmentChatBinding
import com.example.dragon_squire_app.ui.notifications.NotificationsViewModel
import com.example.dragon_squire_app.utils.DSProperties
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val json = Json { ignoreUnknownKeys = true }
    private val url = DSProperties.activeUrl

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    val numero = (1..1000).random()
    val nada = "nada"
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bundle = this.arguments
        val player = SessionData.username

        val messages = mutableListOf<Message>()


        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("messages")) {
                Log.d("ws", "messages: ${savedInstanceState.getString("messages")}")
                val messagesString = savedInstanceState.getString("messages")
                if (messagesString != null) {
                    val messagesList = Json.decodeFromString<List<Message>>(messagesString)
                    messages.addAll(messagesList)
                    Log.d("messages", "messages: $messages")
                }
            } else {
                Log.d("ws", "savedInstanceState: vacia")
            }
        }
        ViewModelProvider(this)[NotificationsViewModel::class.java]

        val liveDataMessage = MutableLiveData(SessionData.messages)



        _binding = FragmentChatBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val recyclerView = binding.recyclerView
        val client = OkHttpClient()
        lateinit var request: Request
        val messageAdapter = MessageAdapter(messages, "user", liveDataMessage)
        SessionData.prueba.observe(this, Observer {
            Log.d("ws", "Message added: $it")
            //SessionData.messages.add(it)

            if (!SessionData.messages.contains(it)) {
                SessionData.messages.add(it)
            }
            messageAdapter.add(it)
            recyclerView.scrollToPosition(messageAdapter.itemCount - 1)
            //SessionData.ws.send(json.encodeToString(it))
        })
        messageAdapter.setMessageListener(object : MessageListener {
            override fun onMessageAdded(message: Message) {
                Log.d("ws", "Message added: $message")
            }
        })

        binding.btnChatSend.setOnClickListener {
            val message = binding.textChatSend.text.toString()
            val listener = WebSocketListenerChat()

            if (message.isNotEmpty()) {
                Log.d("ws", "Message sent: $message")
                SessionData.wsChat.send(message)
                SessionData.prueba.postValue(Message(message, SessionData.username, "all", 1))
                binding.textChatSend.text.clear()
            }


        }

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = messageAdapter


        return root

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d("chat", "onSaveInstanceState")
        val json: Json = Json
        outState.putString("messages", json.encodeToString(SessionData.messages))
    }

    override fun onResume() {
        super.onResume()
        Log.d("chat", "onResume")
        Log.d("chat", "messages: ${SessionData.messages}")

    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("chat", "onDestroyView")
        this._binding = null

    }
}