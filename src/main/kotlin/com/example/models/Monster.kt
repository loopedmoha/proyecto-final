package com.example.models

import com.example.models.Inventory
import kotlinx.serialization.Serializable
import com.example.models.stats.CharacterStats
import java.util.*

@Serializable
data class Monster(
    val uuid: String = UUID.randomUUID().toString(),
    val name: String,
    val stats: CharacterStats,
    val lootTable: Inventory? = null
)
