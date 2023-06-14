package com.example.dragon_squire_app.models.stats

import kotlinx.serialization.Serializable
import com.example.dragon_squire_app.models.Skill

@Serializable
data class Proficiencies(
    val proficientSkills: MutableList<Skill>
)
