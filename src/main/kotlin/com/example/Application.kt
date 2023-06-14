package com.example

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.example.plugins.*
import io.ktor.http.*
import io.ktor.server.plugins.cors.routing.*
import org.koin.core.component.KoinComponent

class KoinApp : KoinComponent {
    fun run() {
        embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
            .start(wait = true)
    }
}

fun main(args: Array<String>): Unit = EngineMain.main(args)



@Suppress("unused") // Referenced in application.conf
fun Application.module() {
    configureKoin()

    configureAuthentication()
    configureSockets()
    configureSerialization()

    configureRouting()
    configureCors()

}
