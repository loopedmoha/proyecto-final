package com.example.dragon_squire_app.chat.holders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dragon_squire_app.R
import com.example.dragon_squire_app.chat.models.Message


class ReceivedMessageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var messageText: TextView
    var timeText: TextView

    init {
        messageText = itemView.findViewById(R.id.text_message_other) as TextView
        timeText = itemView.findViewById(R.id.text_time_other) as TextView
    }

    fun bind(message: Message) {
        messageText.text = message.message

        // Format the stored timestamp into a readable String using method.
        timeText.text = "00:00"
    }
}
