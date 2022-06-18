package com.example.routings

import com.example.db.Repo
import com.example.models.Note
import com.example.restApi.response.SimpleResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.locations.*
import io.ktor.server.locations.post
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


const val NOTES = "$API_VERSION/notes"
const val CREATE_NOTES = "$NOTES/create"
const val UPDATE_NOTES = "$NOTES/update"
const val DELETE_NOTES = "$NOTES/delete"


@Location(CREATE_NOTES)
class NoteCreateRoute

@Location(NOTES)
class NoteGetRoute

@Location(UPDATE_NOTES)
class NoteUpdateRoute

@Location(DELETE_NOTES)
class NoteDeleteRoute


fun Route.noteRoutes(db: Repo) {
    post<NoteCreateRoute> {
        val note = try {
            call.receive<Note>()
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Missing Fields"))
            return@post
        }

        try {
//            val email = call.principal<User>()!!.email
            db.addNote(note)
            call.respond(HttpStatusCode.OK, SimpleResponse(true, "Note Added Successfully"))
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Some Problem occurred"))
        }
    }

    get<NoteGetRoute> {
        try {
//            val email = call.principal<User>()!!.email
            val email = call.request.queryParameters["email"]!!
            val notes = db.getAllNotes(email)
            call.respond(HttpStatusCode.OK, notes)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, emptyList<Note>())
        }
    }

    post<NoteUpdateRoute> {
        val note = try {
            call.receive<Note>()
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Missing Fields"))
            return@post
        }

        try {
//            val email = call.principal<User>()!!.email
//            db.updateNote(note, email)
            db.updateNote(note)
            call.respond(HttpStatusCode.OK, SimpleResponse(true, "Note updated Successfully"))
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Some Problem occurred"))
        }
    }

    delete<NoteDeleteRoute> {
        val noteId = try {
            call.request.queryParameters["id"]!!
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "QueryParameter is not proper"))
            return@delete
        }

        val email = try {
            call.request.queryParameters["email"]!!
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "email is not proper"))
            return@delete
        }

        try {
//            val email = call.principal<User>()!!.email
            db.deleteNote(noteId, email)
            call.respond(HttpStatusCode.OK, SimpleResponse(true, "Note Deleted Successfully"))

        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, e.message ?: "Some Problem occurred")
        }

    }
}