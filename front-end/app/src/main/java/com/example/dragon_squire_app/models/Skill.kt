package com.example.dragon_squire_app.models

import kotlinx.serialization.Serializable

@Serializable
data class Skill(
    val name: String,
    val index: String,
    val characteristic: Characteristic
)

enum class Characteristic {
    STR, DEX, CON, WIS, INT, CHA
}
