package com.example.models.stats

import kotlinx.serialization.Serializable
import com.example.models.Skill

@Serializable
data class Proficiencies(
    val proficientSkills: MutableList<Skill>
)
