package com.example.dragon_squire_app.models

@kotlinx.serialization.Serializable
data class LoggedUser(
    val username : String,
    val token : String
)

