package com.example.models

import com.example.dto.DiceDTO
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

val json : Json = Json {
    prettyPrint = true
    isLenient = true
    ignoreUnknownKeys = true
    encodeDefaults = true
}
@Serializable
data class DicePool(
    var d4: Int = 0,
    var d6: Int = 0,
    var d8: Int = 0,
    var d10: Int = 0,
    var d12: Int = 0,
    var d20: Int = 0,
    var d100: Int = 0,
    var result: Int = 0,
    var public : Boolean = true
){

    fun throwDicePool(): String {
        var result = 0
        for (i in 1..d4) {
            result += (1..4).random()
        }
        for (i in 1..d6) {
            result += (1..6).random()
        }
        for (i in 1..d8) {
            result += (1..8).random()
        }
        for (i in 1..d10) {
            result += (1..10).random()
        }
        for (i in 1..d12) {
            result += (1..12).random()
        }
        for (i in 1..d20) {
            result += (1..20).random()
        }
        for (i in 1..d100) {
            result += (1..100).random()
        }
        this.result = result
        return json.encodeToString(serializer(), this)
    }
}

fun DicePool.toDto(user: String) : DiceDTO {
    return DiceDTO(
        user,
        this.d4,
        this.d6,
        this.d8,
        this.d10,
        this.d12,
        this.d20,
        this.d100,
        this.result,
        this.public
    )
}
