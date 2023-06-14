package com.example.models

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
