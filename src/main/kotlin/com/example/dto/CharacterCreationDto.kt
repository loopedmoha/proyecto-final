package com.example.dto

import com.example.models.player.Player
import com.example.models.stats.Attributes
import com.example.services.classes.CharacterClassService
import com.example.services.races.RaceService
import com.example.services.skills.SkillService
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class CharacterCreationDto(
    val creator: String,
    val name: String,
    val race: String,
    val characterClass: String,
    val strength: Int,
    val dexterity: Int,
    val constitution: Int,
    val intelligence: Int,
    val wisdom: Int,
    val charisma: Int
)

suspend fun CharacterCreationDto.toModel(
    raceService: RaceService,
    characterClassService: CharacterClassService,
    skillService: SkillService
): Player {
    val race = raceService.findByName(this.race) ?: raceService.findByName("Human")
    val characterClass = characterClassService.findById(this.characterClass) ?: characterClassService.findById("Fighter")
    val speed = when (race?.name) {
        "Dwarf" -> 25
        "Elf" -> 30
        "Human" -> 25
        else -> 30
    }

    val armorClass = (this.dexterity - 10) / 2 + 10
    val initiative = (this.dexterity - 10) / 2
    val hitPoints = characterClass?.hitDice?.let { (this.constitution - 10) / 2 + it } ?: 0
    val hitDices = characterClass?.hitDice ?: 0
    val deathSaves = 0
    val attributes = Attributes(
        this.strength,
        this.dexterity,
        this.constitution,
        this.intelligence,
        this.wisdom,
        this.charisma
    )
    val characterStats = com.example.models.stats.CharacterStats(
        attributes,
        armorClass,
        initiative,
        speed,
        hitPoints,
        hitDices,
        deathSaves
    )

    val skills = skillService.findAll()

    return Player(UUID.randomUUID().toString(), creator, name, characterStats, race!!, characterClass!!, skills)
}


