package com.example.db

import com.example.db.dbConnection.DatabaseFactory.dbQuery
import com.example.db.tables.NoteTable
import com.example.db.tables.UserTable
import com.example.models.Note
import com.example.models.User
import org.jetbrains.exposed.sql.*

class Repo {

    suspend fun addUser(user: User) {
        dbQuery {
            UserTable.insert { ut ->
                ut[UserTable.userId] = user.userId
                ut[UserTable.name] = user.name
                ut[UserTable.email] = user.email
                ut[UserTable.cNumber] = user.cNumber
                ut[UserTable.hashPassword] = user.password
            }
        }
    }


    suspend fun findUserByEmail(email: String) = dbQuery {
        UserTable.select {
            UserTable.email.eq(email)
        }.map { rowToUser(it) }.singleOrNull()

    }

    private fun rowToUser(row: ResultRow?): User? {
        if (row == null) {
            return null
        }

        return User(
            userId = row[UserTable.userId],
            name = row[UserTable.name],
            email = row[UserTable.email],
            cNumber = row[UserTable.cNumber],
            password = row[UserTable.hashPassword]
        )
    }


    /*----------------------- Notes Crud  -----------------------*/
    suspend fun addNote(note: Note, email: String) {
        dbQuery {
            NoteTable.insert { nt ->
                nt[NoteTable.id] = note.id
                nt[NoteTable.userEmail] = email
                nt[NoteTable.noteTitle] = note.noteTitle
                nt[NoteTable.description] = note.description
                nt[NoteTable.date] = note.date
            }
        }
    }

    suspend fun addNote(note: Note) {
        dbQuery {
            NoteTable.insert { nt ->
                nt[NoteTable.id] = note.id
                nt[NoteTable.userEmail] = note.userEmail
                nt[NoteTable.noteTitle] = note.noteTitle
                nt[NoteTable.description] = note.description
                nt[NoteTable.date] = note.date
            }
        }
    }

    suspend fun getAllNotes(email: String): List<Note?> = dbQuery {
        NoteTable.select {
            NoteTable.userEmail.eq(email)
        }.map {
            rowToNote(it)
        }
    }

    suspend fun updateNote(note: Note) {
        dbQuery {
            NoteTable.update(
                where = {
                    NoteTable.userEmail.eq(note.userEmail) and NoteTable.id.eq(note.id)
                }
            ) { nt ->
                nt[NoteTable.noteTitle] = note.noteTitle
                nt[NoteTable.description] = note.description
                nt[NoteTable.date] = note.date

            }
        }
    }

    suspend fun deleteNote(id: String, email: String) {
        dbQuery {
            NoteTable.deleteWhere {
                NoteTable.userEmail.eq(email) and NoteTable.id.eq(id)
            }
        }
    }

    private fun rowToNote(row: ResultRow?): Note? {

        if (row == null) {
            return null
        }

        return Note(
            id = row[NoteTable.id],
            userEmail=row[NoteTable.userEmail],
            noteTitle = row[NoteTable.noteTitle],
            description = row[NoteTable.description],
            date = row[NoteTable.date],
        )
    }
}