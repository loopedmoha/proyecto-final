package com.example.dragon_squire_app.models.stats

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

fun Attributes.toSingleAttributes() : List<SingleAttribute>{
    return listOf(
        SingleAttribute("STR", strength, (strength - 10) /2),
        SingleAttribute("DEX", dexterity, (dexterity - 10) /2),
        SingleAttribute("CON", constitution, (constitution - 10) /2),
        SingleAttribute("INT", intelligence, (intelligence - 10) /2),
        SingleAttribute("WIS", wisdom, (wisdom - 10) /2),
        SingleAttribute("CHA", charisma, (charisma - 10) /25)
    )
}
