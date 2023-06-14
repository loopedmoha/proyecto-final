package com.example.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.netty.*
import io.ktor.server.response.*

fun Application.configureAuthentication() {

    install(Authentication) {
        jwt("auth"){
            verifier(
                JWT
                    .require(Algorithm.HMAC256("dragon-squire-secret"))
                    .withIssuer("dragon-squire-issuer")
                    .build()
            )
            validate { credential ->
                if (credential.payload.getClaim("username").asString() != "") {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }

            }

        }

    }
}

