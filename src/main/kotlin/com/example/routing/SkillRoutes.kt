package com.example.routing

import com.example.services.skills.SkillService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.skillRoutes() {
    val skillService: SkillService by inject()
    val endpoint = "/skills"
    routing {
        route(endpoint) {
            get {
                call.respond(HttpStatusCode.OK, skillService.findAll())
            }

            get("/{id}") {
                val id = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest)
                val skill = skillService.findById(id) ?: return@get call.respond(HttpStatusCode.NotFound)
                call.respond(HttpStatusCode.OK, skill)
            }

        }
    }

}