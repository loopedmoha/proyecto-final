package com.example.plugins

import com.example.routing.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }


    }

    itemRoutes()
    skillRoutes()
    raceRoutes()
    securityRoutes()
    playerRoutes()
    monsterRoutes()
}




