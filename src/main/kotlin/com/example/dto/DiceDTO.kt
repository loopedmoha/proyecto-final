package com.example.dto

import com.example.models.DicePool
import kotlinx.serialization.Serializable

@Serializable
data class DiceDTO(
    var user : String,
    var d4: Int = 0,
    var d6: Int = 0,
    var d8: Int = 0,
    var d10: Int = 0,
    var d12: Int = 0,
    var d20: Int = 0,
    var d100: Int = 0,
    var result: Int = 0,
    var public : Boolean = true
)

