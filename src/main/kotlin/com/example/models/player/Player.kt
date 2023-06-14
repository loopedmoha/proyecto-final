package com.example.models.player

import kotlinx.serialization.Serializable
import com.example.models.Inventory
import com.example.models.Skill
import com.example.models.stats.CharacterStats
import com.example.utils.serializer.UUIDSerializer
import java.util.*

@Serializable
data class Player(
    val uuid: String = UUID.randomUUID().toString(),
    val creator : String,
    val name: String,
    val characterStats: CharacterStats,
    val race: Race,
    val characterClass: CharacterClass,
    val skills: List<Skill>,
    val inventory: Inventory? = null
)
