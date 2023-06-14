package com.example.dragon_squire_app.models

import kotlinx.serialization.Serializable

@Serializable

data class Value(
    val cobre: Int,
    val plata: Int,
    val oro: Int
)
