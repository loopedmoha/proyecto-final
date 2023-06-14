package com.example.models

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Npc(
    val uuid: String = UUID.randomUUID().toString(),
    val creador : String,
    val name: String,
    val ubicacion: String,
    val descripcion: String,
    val notas: MutableList<String>
)
