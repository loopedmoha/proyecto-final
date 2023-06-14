package com.example.models

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Encounter(
    val creator : String,
    val uuid: String = UUID.randomUUID().toString(),
    val enemies : List<Monster>,
    val loot : Inventory
)
