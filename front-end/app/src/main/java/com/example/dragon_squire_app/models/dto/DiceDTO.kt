package com.example.dragon_squire_app.models.dto
import com.example.dragon_squire_app.models.DicePool
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

fun DiceDTO.toDicePool() : DicePool {
    return DicePool(
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

