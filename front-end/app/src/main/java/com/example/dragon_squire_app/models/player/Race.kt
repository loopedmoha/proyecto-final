package com.example.dragon_squire_app.models.player

import kotlinx.serialization.Serializable
import com.example.dragon_squire_app.models.stats.Proficiencies


@Serializable
data class Race(
    val name: String,
    val proficiencies: Proficiencies
)
