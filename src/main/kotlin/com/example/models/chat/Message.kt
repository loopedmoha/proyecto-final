package com.example.models.chat

import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val message : String,
    val sender : String,
    val receiver : String,
    val timestamp : Long
)
