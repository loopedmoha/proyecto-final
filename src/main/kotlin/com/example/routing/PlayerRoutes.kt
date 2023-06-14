package com.example.routing

import com.example.dto.CharacterCreationDto
import com.example.dto.toModel
import com.example.exceptions.ItemDuplicated
import com.example.exceptions.ItemUnauthorized
import com.example.models.Item
import com.example.models.player.Player
import com.example.services.classes.CharacterClassService
import com.example.services.players.PlayerService
import com.example.services.races.RaceService
import com.example.services.skills.SkillService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.koin.ktor.ext.inject
import org.litote.kmongo.json
import java.util.UUID

fun Application.playerRoutes() {

    val endpoint = "/player"
    val playerService: PlayerService by inject()
    val raceService: RaceService by inject()
    val characterClassService: CharacterClassService by inject()
    val skillService: SkillService by inject()
    val json = Json { ignoreUnknownKeys = true }
    routing {
        route(endpoint) {
            get("/{id}") {
                val id = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest)
                val res = playerService.findById(UUID.fromString(id))
                res?.let {
                    call.respond(HttpStatusCode.OK, res)
                } ?: run { call.respond(HttpStatusCode.BadRequest) }
            }

            get("/creator/{creator}") {
                val creator = call.parameters["creator"] ?: return@get call.respond(HttpStatusCode.BadRequest)
                val res = playerService.findByCreator(creator)
                println(res)
                call.respond(HttpStatusCode.OK, res)
            }

            post {
                try {
                    val body = call.receiveText()
                    println(body)
                    val playerDto = json.decodeFromString<CharacterCreationDto>(body)
                    val player = playerDto.toModel(raceService, characterClassService, skillService)
                    println(player)
                    playerService.save(player)
                    call.respond(HttpStatusCode.Created, player)
                } catch (e: Exception) {
                    e.printStackTrace()
                    call.respond(HttpStatusCode.Unauthorized, e.message.toString())
                } catch (e: Exception) {
                    e.printStackTrace()
                    call.respond(HttpStatusCode.Conflict, e.message.toString())
                }
            }

            delete("/{id}") {
                val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
                val res = playerService.deleteById(UUID.fromString(id))
                res.let {
                    call.respond(HttpStatusCode.OK, res)
                }
                call.respond(HttpStatusCode.NotFound)
            }


        }
    }
}