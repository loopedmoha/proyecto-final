package com.example.dragon_squire_app.models.player

import kotlinx.serialization.Serializable
import com.example.dragon_squire_app.models.Inventory
import com.example.dragon_squire_app.models.stats.CharacterStats
import com.example.dragon_squire_app.models.Skill
import java.util.*

@Serializable
data class Player(
    val uuid: String = UUID.randomUUID().toString(),
    val creator : String,
    val name: String,
    val characterStats: CharacterStats,
    val race: Race,
    val characterClass: CharacterClass,
    val level : Int = 1,
    val skills: List<Skill>,
    val inventory: Inventory? = null
)
