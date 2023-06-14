package com.example.dragon_squire_app.models

import java.util.UUID

@kotlinx.serialization.Serializable
data class User(
    val uuid: String,
    val username : String,
    val password : String,
    val role : String
)
