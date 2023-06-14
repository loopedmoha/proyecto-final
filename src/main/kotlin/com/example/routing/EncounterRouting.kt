package com.example.routing

import com.example.exceptions.ItemDuplicated
import com.example.exceptions.ItemUnauthorized
import com.example.models.Encounter
import com.example.models.Item
import com.example.models.Npc
import com.example.services.misc.EncounterService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.encounterRouting() {
    val endpoint = "/encounters"
    val encounterService: EncounterService by inject()

    routing {
        route(endpoint) {
            get {
                call.respond(HttpStatusCode.OK, encounterService.findAll())
            }

            get("/{id}") {
                val id = call.parameters["id"]
                id?.let {
                    val res = encounterService.findById(id)
                    res?.let { call.respond(HttpStatusCode.OK, res) }
                        ?.run { call.respond(HttpStatusCode.NotFound) }
                }
                call.respond(HttpStatusCode.BadRequest)
            }

            get("/creator/{creator}") {
                val creator = call.parameters["creator"]
                creator?.let {
                    val res = encounterService.findByCreator(creator)
                    call.respond(HttpStatusCode.OK, res)
                }
                call.respond(HttpStatusCode.BadRequest)
            }

            post {
                try {
                    val item = call.receive<Encounter>()
                    println(item)
                    encounterService.save(item)
                    call.respond(HttpStatusCode.Created, item)
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest)
                    e.printStackTrace()
                }
            }

            put {
                try {
                    val item = call.receive<Encounter>()
                    encounterService.update(item)
                    call.respond(HttpStatusCode.OK, item)
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.Unauthorized, e.message.toString())
                }
            }

            delete("/{id}") {
                val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
                val res = encounterService.deleteById(id)
                if (res) {
                    call.respond(HttpStatusCode.OK)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }
        }
    }
}