package com.example.models

import com.example.utils.serializer.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID


@Serializable
data class Item(

    val uuid: String = UUID.randomUUID().toString(),
    val nombre: String,
    val precio: Value,
    val descripcion: String,
    val categoria: Categoria,
    var magical: Boolean = false,
    var magicalTrait: String? = null,
    var vanilla : Boolean = true,
    var creator : String? = null
)

enum class Categoria {
    ARMA,
    ARMADURA,
    OBJETO,
    CONSUMIBLE
}
