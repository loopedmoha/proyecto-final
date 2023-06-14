package com.example.dragon_squire_app.models.player

import kotlinx.serialization.Serializable
import com.example.dragon_squire_app.models.stats.Proficiencies

@Serializable
data class CharacterClass(
    val name: String,
    val hitDice: Int,
    val proficiencies: Proficiencies
)
