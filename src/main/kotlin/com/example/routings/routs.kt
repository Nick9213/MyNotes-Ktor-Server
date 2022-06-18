package com.example.routings

import com.example.db.Repo
import com.example.models.User
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*
import kotlin.streams.asSequence

fun Application.configRouts() {

    val db = Repo()

    routing {

        ///Single query parameter
        get("/") {
            call.respondText("Hello this is Ktor Crud")
        }

        loginRegisterRout(db)
        noteRoutes(db)

        //Multiple query parameter
        get("/token") {
            val name = call.request.queryParameters["name"]!!
            val email = call.request.queryParameters["email"]!!
            val cNumber = call.request.queryParameters["cNumber"]!!
            val password = call.request.queryParameters["password"]!!
            val source = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "abcdefghijklmnopqrstuvwxyz" + "0123456789"
            val id: String = Random().ints(12, 0, source.length).asSequence().map(source::get).joinToString("")
            val user = User(id, name, email, cNumber, password)
            call.respond(user)
        }


        route("/user") {
            post {
                val body = call.receive<String>()
                call.respond(body)
            }
        }
    }
}