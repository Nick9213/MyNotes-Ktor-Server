package com.example.models

import io.ktor.server.auth.*

data class User(val userId: String, val name: String, val email: String, val cNumber: String, val password: String):Principal
