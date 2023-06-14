package com.example.routing

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.models.CurrentSessions
import com.example.models.User
import com.example.services.TokensService
import com.example.services.misc.UserService
import com.example.utils.KeyGenerator
import com.example.utils.PasswordEncrypter
import io.ktor.http.*
import io.ktor.http.ContentType.Application.Json
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
import java.util.*

fun Application.securityRoutes() {
    val properties = environment.config
    val secret = properties.property("jwt.secret").getString()
    //val secret = environment.config.property("jwt.secret").getString()
    val issuer = environment.config.property("jwt.issuer").getString()
    val audience = environment.config.property("jwt.audience").getString()
    val myRealm = environment.config.property("jwt.realm").getString()

    val loggedUsers = mutableListOf<User>()
    val userService: UserService by inject()
    val tokenService: TokensService by inject()
    val json = Json { ignoreUnknownKeys = true }
    routing {

        get("/checkSession/{sessionName}") {
            val sessionName = call.parameters["sessionName"]

            if (sessionName == null) {
                call.respond(HttpStatusCode.NotFound, "Missing sessionName")
            }
            if (CurrentSessions.sessions.contains(sessionName)) {
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
        get("/newSession") {
            var sessionName = ""
            while (sessionName == "" || CurrentSessions.sessions.contains(sessionName)) {
                sessionName = KeyGenerator.generateKey()
            }

            CurrentSessions.sessions.add(sessionName)
            call.respond(HttpStatusCode.OK, sessionName)
        }

        get("/create/{sessionName}") {
            println("CREATE SESSION")
            val sessionName = call.parameters["sessionName"]
            if (sessionName == null) {
                call.respond(HttpStatusCode.BadRequest)
            } else {
                if (CurrentSessions.sessions.contains(sessionName)) {
                    call.respond(HttpStatusCode.Conflict)
                }
                CurrentSessions.sessions.add(sessionName)
                println("SESSIONS : ${CurrentSessions.sessions}")
                //CurrentSessions.usersSession[sessionName] = 1
                call.respond(HttpStatusCode.OK, sessionName)
            }
        }


        post("/login") {
            try {
                println("SECRETO : $secret")
                val body = call.receiveText()
                println("BODY : $body")
                val user = json.decodeFromString(User.serializer(), body)
                userService.findByUserName(user.username)?.let {
                    if (!PasswordEncrypter.checkPassword(user.password, it.password)) {
                        call.respond(HttpStatusCode.BadRequest, "ERROR DE LOGEO")
                    }
                } ?: run {
                    call.respond(HttpStatusCode.NotFound, "ERROR DE LOGEO")
                }
                println(user)
                val token = JWT.create()
                    .withAudience(audience)
                    .withIssuer(issuer)
                    .withClaim("username", user.username)
                    .withExpiresAt(Date(System.currentTimeMillis() + 10000))
                    .sign(Algorithm.HMAC256(secret))
                println(loggedUsers)

                call.respond(HttpStatusCode.OK, mapOf("token" to token))
            } catch (e: Exception) {
                e.printStackTrace()
                call.respond(HttpStatusCode.BadRequest, "ERROR DE LOGEO")
            } finally {
            }

        }

        post("/register") {
            try {
                val body = call.receiveText()
                val user = json.decodeFromString<User>(body)
                userService.findByUserName(user.username)?.let {
                    call.respond(HttpStatusCode.BadRequest, "USUARIO YA EXISTE")
                }
                println(user)
                userService.save(user)
                val token = JWT.create()
                    .withAudience(audience)
                    .withIssuer(issuer)
                    .withClaim("username", user.username)
                    .withExpiresAt(Date(System.currentTimeMillis() + 10000))
                    .sign(Algorithm.HMAC256(secret))
                call.respond(mapOf("token" to token))
            } catch (e: Exception) {
                e.printStackTrace()
                call.respond(HttpStatusCode.BadRequest, "ERROR DE LOGEO")
            }

        }
        get("/jwt/{jwt}") {
            try {
                val jwt = call.parameters["jwt"]
                val token = JWT.decode(jwt)
                val username = token.getClaim("username").asString()
                if (token.expiresAt.before(Date())) {
                    call.respond(HttpStatusCode.Unauthorized, "Token expirado")
                }
                call.respond(HttpStatusCode.Unauthorized, "Token expirado")

                val user = userService.findByUserName(username)
                user?.let {
                    call.respond(HttpStatusCode.OK, "USUARIO AUTENTICADO")
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Unauthorized, "Usuario no encontrado o no autenticado")
            }

        }
        authenticate("auth") {
            @Suppress("unused")
            post("/logoff") {
                try {
                    val jwt = call.principal<JWTPrincipal>()
                    val username = jwt?.payload?.getClaim("username")
                        .toString().replace("\"", "")
                    val user = userService.findByUserName(username)
                    user?.let {
                        loggedUsers.removeIf { user -> user.username == username }
                        call.respond(HttpStatusCode.OK, "SESION CERRADA")
                    }
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.Unauthorized, "Usuario no encontrado o no autenticado")
                }
            }


        }


    }
}