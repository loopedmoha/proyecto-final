package com.example.routing

import com.example.exceptions.ItemDuplicated
import com.example.exceptions.ItemUnauthorized
import com.example.services.items.ItemService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.example.models.Item
import com.example.services.misc.UserService
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import org.koin.ktor.ext.inject

fun Application.itemRoutes() {
    val itemService: ItemService by inject()
    val userService: UserService by inject()
    val endpoint = "/items"
    routing {
        route(endpoint) {
            get {
                call.respond(HttpStatusCode.OK, itemService.findAll())
            }
            get("/{id}") {
                val id = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest)
                val item = itemService.findById(id) ?: return@get call.respond(HttpStatusCode.NotFound)
                call.respond(HttpStatusCode.OK, item)
            }
            authenticate("auth") {
                post {

                    try {
                        val jwt = call.principal<JWTPrincipal>()
                        val username = jwt?.payload?.getClaim("username")
                            .toString().replace("\"", "")
                        val user = userService.findByUserName(username)
                        if (user == null) {
                            call.respond(HttpStatusCode.Unauthorized, "Not logged in")
                        }
                        val item = call.receive<Item>()
                        println(item)
                        itemService.save(item)
                        call.respond(HttpStatusCode.Created, item)
                    } catch (e: ItemUnauthorized) {
                        call.respond(HttpStatusCode.Unauthorized, e.message.toString())
                    } catch (e: ItemDuplicated) {
                        call.respond(HttpStatusCode.Conflict, e.message.toString())
                    }
                }
                put("/{id}") {
                    try {
                        val item = call.receive<Item>()
                        itemService.update(item)
                        call.respond(HttpStatusCode.OK, item)
                    } catch (e: ItemUnauthorized) {
                        call.respond(HttpStatusCode.Unauthorized, e.message.toString())
                    } catch (e: ItemDuplicated) {
                        call.respond(HttpStatusCode.Conflict, e.message.toString())
                    }
                }
                delete("/{id}") {
                    val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
                    val res = itemService.deleteById(id)
                    if (res) {
                        call.respond(HttpStatusCode.OK)
                    } else {
                        call.respond(HttpStatusCode.NotFound)
                    }
                }
            }
        }
    }


}





fun Application.classRoutes() {

}