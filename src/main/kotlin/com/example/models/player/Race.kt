package com.example.models.player

import kotlinx.serialization.Serializable
import com.example.models.stats.Proficiencies


@Serializable
data class Race(
    val name: String,
    val proficiencies: Proficiencies
)
