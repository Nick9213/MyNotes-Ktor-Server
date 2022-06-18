package com.example

import com.example.db.dbConnection.DatabaseFactory
import io.ktor.server.netty.*
import com.example.plugins.*
import com.example.routings.configRouts
import io.ktor.server.application.*
import io.ktor.server.locations.*

fun main(args: Array<String>): Unit = EngineMain.main(args)

@Suppress("unused")
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {

    DatabaseFactory.initDatabase()

    install(Locations)

    configRouts()

    configureSerialization()
    configurationSession()
}