package com.example.models

import kotlinx.serialization.Serializable

@Serializable

data class Value(
    val cobre: Int,
    val plata: Int,
    val oro: Int
)
