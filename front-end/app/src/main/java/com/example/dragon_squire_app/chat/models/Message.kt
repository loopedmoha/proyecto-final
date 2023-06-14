package com.example.dragon_squire_app.chat.models

import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val message : String,
    val sender : String,
    val receiver : String,
    val timestamp : Long = System.currentTimeMillis()
)