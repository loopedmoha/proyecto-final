package com.example.dragon_squire_app.utils

import com.example.dragon_squire_app.models.dto.CharacterCreationDto
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.kotlinx.serializer.*

interface IApiService {
    suspend fun login(username: String, password: String): String
suspend fun register(username: String, password: String): String
    suspend fun checkSession(sessionName : String): Boolean
    suspend fun createSession(): String

    suspend fun createNamedSession(sessionName: String): String

    suspend fun createCharacter(character: CharacterCreationDto): String

    suspend fun getCharacter(username: String): String
    companion object {
        fun create(): IApiService {
            return ApiService(
                client = HttpClient(Android)
            )


        }
    }
}