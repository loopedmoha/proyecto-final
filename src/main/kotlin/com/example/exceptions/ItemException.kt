package com.example.exceptions


sealed class ItemException(message: String) : RuntimeException(message)

class ItemNotFoundException(message: String) : ItemException(message)
class ItemBadRequest(message: String) : ItemException(message)
class ItemUnauthorized(message: String) : ItemException(message)
class ItemDuplicated(message: String) : ItemException(message)