package com.example.dragon_squire_app.models.stats

import kotlinx.serialization.Serializable

@Serializable
data class CharacterStats(
    val attributes: Attributes,
    var armorClass : Int,
    var initiative : Int,
    var speed : Int,
    var hitPoints : Int,
    var hitDices : Int,
    var deathSaves : Int
)
