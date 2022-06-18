package com.example.plugins

import io.ktor.server.application.*
import io.ktor.server.sessions.*

fun Application.configurationSession(){
    data class MySession(val count: Int = 0)
    install(Sessions) {
        cookie<MySession>("MY_SESSION") {
            cookie.extensions["SameSite"] = "lax"
        }
    }
}