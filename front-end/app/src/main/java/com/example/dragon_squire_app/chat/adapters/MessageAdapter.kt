package com.example.dragon_squire_app.chat.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.dragon_squire_app.R
import com.example.dragon_squire_app.chat.holders.SentMessageHolder
import com.example.dragon_squire_app.chat.models.Message
import com.example.dragon_squire_app.utils.SessionData

class MessageAdapter(
    private val messages: MutableList<Message>,
    private val user: String,
    private val messageLiveData: MutableLiveData<MutableList<Message>>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val MESSAGE_SENT = 1
    private val MESSAGE_RECEIVED = 2

    private var messageListener: MessageListener? = null

    fun setMessageListener(listener: MessageListener) {
        messageListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == MESSAGE_SENT) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.message_me_view, parent, false)
            SentViewHolder(view)
        }else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.message_others_view, parent, false)
            ReceivedMessageHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    fun add(message: Message) {
        //messages.clear()
        if(messages.contains(message)){
            return
        }
        messages.add(message)
        if(!SessionData.messages.contains(message)){
            SessionData.messages.add(message)
            Log.d("messages", "messages: $messages")
            notifyItemInserted(messages.size - 1)
        }

        //messageListener?.onMessageAdded(message)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder.itemViewType == MESSAGE_SENT)
            (holder as SentViewHolder).bind(messages[position])
        else
            (holder as ReceivedMessageHolder).bind(messages[position])
    }

    override fun getItemViewType(position: Int): Int {
        if (messages[position].sender == SessionData.username) {
            return 1
        }
        return super.getItemViewType(position)
    }

    inner class ReceivedMessageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var messageText: TextView = itemView.findViewById(R.id.text_message_other)
        private var timeText: TextView = itemView.findViewById(R.id.text_time_other)
        private var nameText: TextView = itemView.findViewById(R.id.text_name_other)

        fun bind(message: Message) {
            messageText.text = message.message

            // Format the stored timestamp into a readable String using method.
            timeText.text = "00:00"
            nameText.text = message.sender
        }
    }

    inner class SentViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private var messageText: TextView = itemView.findViewById(R.id.text_message_me) as TextView

        private var timeText: TextView =
            itemView.findViewById(R.id.text_me_message_time) as TextView

        fun bind(message: Message) {
            messageText.text = message.message

            // Format the stored timestamp into a readable String using method.
            timeText.text = "00:00"
        }
    }
}

interface MessageListener {
    fun onMessageAdded(message: Message)
}
