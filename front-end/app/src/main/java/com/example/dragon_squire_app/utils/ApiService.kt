package com.example.dragon_squire_app.utils

import android.content.res.AssetManager
import android.util.Log
import com.example.dragon_squire_app.models.User
import com.example.dragon_squire_app.models.dto.CharacterCreationDto
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.util.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.UUID

class ApiService(private val client: HttpClient) : IApiService {
    val json = Json
    private var API_URL = DSProperties.activeUrl

    @OptIn(InternalAPI::class)
    override suspend fun login(username: String, password: String): String {
        API_URL = DSProperties.activeUrl
        val user = User(UUID.randomUUID().toString(), username, password, "USER")
        Log.d("LOGIN", json.encodeToString(user))
        return try {
            val res = client.post("http://$API_URL/login") {
                body = json.encodeToString(user)
            }
            val body = res.bodyAsText()
            Log.d("LOGIN", body)
            when (res.status) {
                HttpStatusCode.OK -> {
                    body
                }
                HttpStatusCode.NotFound -> {
                    "NOT_FOUND"
                }
                HttpStatusCode.BadRequest -> {
                    "BAD_REQUEST"
                }
                else -> {
                    "ERROR"
                }
            }
        } catch (e: Exception) {
            "ERROR"
        }
    }

    @OptIn(InternalAPI::class)
    override suspend fun register(username: String, password: String): String {
        API_URL = DSProperties.activeUrl
        val user = User(UUID.randomUUID().toString(), username, password, "USER")
        Log.d("REGISTER", json.encodeToString(user))
        return try {
            val res = client.post("http://$API_URL/register") {
                body = json.encodeToString(user)
            }.bodyAsText()
            Log.d("REGISTER", res)
            res
        } catch (e: Exception) {
            "ERROR"
        }
    }

    override suspend fun checkSession(sessionName: String): Boolean {
        API_URL = DSProperties.activeUrl
        return try {
            val response = client.get("http://$API_URL/checkSession/$sessionName").call.response
            response.status == HttpStatusCode.OK
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun createSession(): String {
        API_URL = DSProperties.activeUrl
        return try {
            val response = client.get("http://$API_URL/newSession").call.response
            response.body<String>()
            Log.d("SESSION", response.body<String>().toString())
            response.body<String>()
        } catch (e: Exception) {
            "ERROR"
        }
    }

    override suspend fun createNamedSession(sessionName: String): String {
        API_URL = DSProperties.activeUrl
        return try {
            val response = client.get("http://$API_URL/create/$sessionName").call.response
            if (response.status == HttpStatusCode.Conflict || response.status == HttpStatusCode.BadRequest) {
                "ERROR"
            } else {
                response.body<String>()
                Log.d("SESSION", response.body<String>().toString())
                response.body<String>()
            }
        } catch (e: Exception) {
            "ERROR"
        }
    }


    @OptIn(InternalAPI::class)
    override suspend fun createCharacter(character: CharacterCreationDto): String {
        Log.d("Character creation", json.encodeToString(character))
        return try {
            val res = client.post("http://$API_URL/player") {
                body = json.encodeToString(character)
            }.bodyAsText()
            Log.d("Character creation", res)
            res
        } catch (e: Exception) {
            e.message.toString()
        }
    }

    override suspend fun getCharacter(username: String): String {

        return try {
            val response = client.get("http://$API_URL/player/creator/$username").call.response
            response.body<String>()
            Log.d("SESSION", response.body<String>().toString())
            response.body<String>()
        } catch (e: Exception) {
            "ERROR"
        }
    }


}