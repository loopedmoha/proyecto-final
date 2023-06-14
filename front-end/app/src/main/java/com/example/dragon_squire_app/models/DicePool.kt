package com.example.dragon_squire_app.models

import com.example.dragon_squire_app.models.dto.DiceDTO
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

    fun addD4() {
        d4 += 1
    }

    fun addD6() {
        d6 += 1
    }

    fun addD8() {
        d8 += 1
    }

    fun addD10() {
        d10 += 1
    }

    fun addD12() {
        d12 += 1
    }

    fun addD20() {
        d20 += 1
    }

    fun addD100() {
        d100 += 1
    }

    fun removeD4() {
        if (d4 > 0) {
            d4 = 0
        }
    }

    fun removeD6() {
        if (d6 > 0) {
            d6 = 0
        }
    }

    fun removeD8() {
        if (d8 > 0) {
            d8 = 0
        }
    }

    fun removeD10() {
        if (d10 > 0) {
            d10 = 0
        }
    }

    fun removeD12() {
        if (d12 > 0) {
            d12 = 0
        }
    }

    fun removeD20() {
        if (d20 > 0) {
            d20 = 0
        }
    }

    fun removeD100() {
        if (d100 > 0) {
            d100 = 0
        }
    }

    fun reset() {
        d4 = 0
        d6 = 0
        d8 = 0
        d10 = 0
        d12 = 0
        d20 = 0
        d100 = 0
    }
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

    fun throwString() : String = "$d4;$d6;$d8;$d10;$d12;$d20;$d100;$public"

}

fun DicePool.toDto(user : String) : DiceDTO{
    return DiceDTO(
        user = user,
        d4 = this.d4,
        d6 = this.d6,
        d8 = this.d8,
        d10 = this.d10,
        d12 = this.d12,
        d20 = this.d20,
        d100 = this.d100,
        result = this.result,
        public = this.public
    )
}



