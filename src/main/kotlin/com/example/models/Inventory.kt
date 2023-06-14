package com.example.models

import kotlinx.serialization.Serializable

@Serializable

data class Inventory(
    val items : MutableList<Item>,
    var goldCoins : Int,
    var silverCoins : Int,
    var copperCoins : Int
)
