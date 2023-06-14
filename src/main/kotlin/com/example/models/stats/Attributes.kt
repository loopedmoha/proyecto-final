package com.example.models.stats

import kotlinx.serialization.Serializable

@Serializable
data class Attributes(
    var strength: Int,
    var dexterity: Int,
    var constitution: Int,
    var intelligence: Int,
    var wisdom: Int,
    var charisma: Int
)
