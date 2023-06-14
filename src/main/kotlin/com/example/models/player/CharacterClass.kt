package com.example.models.player

import kotlinx.serialization.Serializable
import com.example.models.stats.Proficiencies

@Serializable
data class CharacterClass(
    val name: String,
    val hitDice: Int,
    val proficiencies: Proficiencies
)
