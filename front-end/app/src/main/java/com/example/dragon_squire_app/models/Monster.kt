package com.example.dragon_squire_app.models

import kotlinx.serialization.Serializable
import com.example.dragon_squire_app.models.stats.CharacterStats
import java.util.*

@Serializable
data class Monster(
    val uuid: String = UUID.randomUUID().toString(),
    val name: String,
    val stats: CharacterStats,
    val lootTable: Inventory? = null
)
