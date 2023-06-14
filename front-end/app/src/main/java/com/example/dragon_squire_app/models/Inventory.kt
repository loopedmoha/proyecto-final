package com.example.dragon_squire_app.models

import com.example.dragon_squire_app.models.Item
import kotlinx.serialization.Serializable

@Serializable

data class Inventory(
    val items : MutableList<Item>,
    var goldCoins : Int,
    var silverCoins : Int,
    var copperCoins : Int
)
