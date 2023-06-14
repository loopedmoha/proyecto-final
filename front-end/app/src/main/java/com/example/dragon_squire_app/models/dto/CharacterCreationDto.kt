package com.example.dragon_squire_app.models.dto

@kotlinx.serialization.Serializable
data class CharacterCreationDto(
    val creator : String,
    val name : String,
    val race : String,
    val characterClass : String,
    val strength: Int,
    val dexterity: Int,
    val constitution: Int,
    val intelligence: Int,
    val wisdom: Int,
    val charisma: Int
)