package com.example.db.tables

import org.jetbrains.exposed.sql.Table

object UserTable : Table() {
    val userId = varchar("userId", 512)
    val name = varchar("name", 512)
    val email = varchar("email", 512)
    val cNumber = varchar("cNumber", 512)
    val hashPassword = varchar("hashPassword", 512)

    override val primaryKey: PrimaryKey = PrimaryKey(email)
}