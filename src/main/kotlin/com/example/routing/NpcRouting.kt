package com.example.routing

import com.example.exceptions.ItemDuplicated
import com.example.exceptions.ItemUnauthorized
import com.example.models.Monster
import com.example.models.Npc
import com.example.services.misc.NpcService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.npcRouting() {
    val endpoint = "/npc"
    val npcService: NpcService by inject()

    routing {
        route(endpoint) {
            get {
                call.respond(HttpStatusCode.OK, npcService.findAll())
            }

            get("/{id}") {
                val id = call.parameters["id"]
                id?.let {
                    val res = npcService.findById(id)
                    res?.let { call.respond(HttpStatusCode.OK, res) }
                        ?.run { call.respond(HttpStatusCode.NotFound) }
                }
                call.respond(HttpStatusCode.BadRequest)
            }

            get("/creator/{creator}") {
                val creator = call.parameters["creator"]
                creator?.let {
                    val res = npcService.findByCreator(creator)
                    call.respond(HttpStatusCode.OK, res)
                }
                call.respond(HttpStatusCode.BadRequest)
            }

            post {
                try {
                    val item = call.receive<Npc>()
                    println(item)
                    npcService.save(item)
                    call.respond(HttpStatusCode.Created, item)
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest)
                    e.printStackTrace()
                }
            }
        }
    }
}