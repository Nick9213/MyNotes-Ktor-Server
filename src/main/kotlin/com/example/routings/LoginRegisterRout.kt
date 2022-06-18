package com.example.routings

import com.example.constants.randomID
import com.example.db.Repo
import com.example.models.User
import com.example.restApi.request.LoginRequest
import com.example.restApi.request.RegisterRequest
import com.example.restApi.response.SimpleResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.locations.*
import io.ktor.server.locations.post
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

const val API_VERSION = "/v1"
const val USERS = "$API_VERSION/users"
const val REGISTER_REQUEST = "$USERS/register"
const val LOGIN_REQUEST = "$USERS/login"

@Location(REGISTER_REQUEST)
class UserRegisterRoute

@Location(LOGIN_REQUEST)
class UserLoginRoute

fun Route.loginRegisterRout(db: Repo) {

    ///Registration
    post<UserRegisterRoute> {
        val registrationRequest = try {
            call.receive<RegisterRequest>()
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Missing Fields"))
            return@post
        }

        //// store in the database
        try {
            val uuid: String = randomID()
            val user = User(
                uuid,
                registrationRequest.name,
                registrationRequest.email,
                registrationRequest.cNumber,
                registrationRequest.password
//                hasFunction(registrationRequest.password)
            )
            db.addUser(user)
//            call.respond(HttpStatusCode.OK, SimpleResponse(true, jwtService.generateToken(user)))
            call.respond(HttpStatusCode.OK, SimpleResponse(true, "Register Successfully"))
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Some Problem Occurred"))
        }

    }

    //Login
    post<UserLoginRoute> {
        val loginRequest = try {
            call.receive<LoginRequest>()
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Error is " + e.message))
            return@post
        }

        //validation
        try {
            val user = db.findUserByEmail(loginRequest.email)
            if (user == null) {
                call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Wrong Email ID entered"))
            } else {
                call.respond(HttpStatusCode.OK, SimpleResponse(true, "Login Successfully"))

//                call.respond(HttpStatusCode.OK, SimpleResponse(true, jwtService.generateToken(user)))
                /*               if (user.password == hasFunction(user.password)) {
                                   call.respond(HttpStatusCode.OK, SimpleResponse(true, jwtService.generateToken(user)))
                               } else {
                                   call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Password Incorrect"))
                               }*/
            }
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Some Problem Occurred"))

        }
    }


}