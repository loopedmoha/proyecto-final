package com.example.routing

import com.example.exceptions.ItemDuplicated
import com.example.exceptions.ItemUnauthorized
import com.example.models.Item
import com.example.models.Monster
import com.example.services.monsters.MonsterService
import com.example.utils.DataLoader
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.monsterRoutes() {
    val monsterService: MonsterService by inject()
    val endpoint = "/monsters"
    routing {
        route("$endpoint/init") {
            get {
                DataLoader.loadMonsters()
            }
        }

        route(endpoint) {
            get {
                call.respond(HttpStatusCode.OK, monsterService.findAll())
            }
            get("/{id}") {
                val id = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest)
                val item = monsterService.findById(id) ?: return@get call.respond(HttpStatusCode.NotFound)
                call.respond(HttpStatusCode.OK, item)
            }

            post {
                try {
                    println("ESTOY DENTRO")
                    val item = call.receive<Monster>()
                    println(item)
                    monsterService.save(item)
                    call.respond(HttpStatusCode.Created, item)
                } catch (e: ItemUnauthorized) {
                    call.respond(HttpStatusCode.Unauthorized, e.message.toString())
                } catch (e: ItemDuplicated) {
                    call.respond(HttpStatusCode.Conflict, e.message.toString())
                } catch (e : Exception){
                    e.printStackTrace()
                }
            }

            put("/{id}"){
                try {
                    val item = call.receive<Monster>()
                    monsterService.update(item)
                    call.respond(HttpStatusCode.OK, item)
                } catch (e: ItemUnauthorized) {
                    call.respond(HttpStatusCode.Unauthorized, e.message.toString())
                } catch (e: ItemDuplicated) {
                    call.respond(HttpStatusCode.Conflict, e.message.toString())
                }
            }

            delete("/{id}") {
                val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
                val res = monsterService.deleteById(id)
                if (res) {
                    call.respond(HttpStatusCode.OK)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }
        }
    }

}