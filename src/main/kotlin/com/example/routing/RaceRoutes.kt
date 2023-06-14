package com.example.routing

import com.example.services.races.RaceService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.raceRoutes() {
    val raceService : RaceService by inject()
    val endpoint = "/races"

    routing {
        route(endpoint) {
            get {
                call.respond(raceService.findAll())
            }

            get("/{name}"){
                val name = call.parameters["name"] ?: return@get call.respond(HttpStatusCode.BadRequest)
                val item = raceService.findByName(name) ?: return@get call.respond(HttpStatusCode.NotFound)
                call.respond(HttpStatusCode.OK, item)
            }
        }
    }
}