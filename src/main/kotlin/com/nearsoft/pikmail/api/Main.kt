package com.nearsoft.pikmail.api

import com.nearsoft.pikmail.Pikmail
import com.nearsoft.pikmail.ProfileNotFountException
import kotlinx.coroutines.experimental.rx2.await
import org.jetbrains.ktor.content.default
import org.jetbrains.ktor.content.files
import org.jetbrains.ktor.content.static
import org.jetbrains.ktor.content.staticRootFolder
import org.jetbrains.ktor.host.embeddedServer
import org.jetbrains.ktor.http.ContentType
import org.jetbrains.ktor.http.HttpStatusCode
import org.jetbrains.ktor.netty.Netty
import org.jetbrains.ktor.response.respondRedirect
import org.jetbrains.ktor.response.respondText
import org.jetbrains.ktor.routing.get
import org.jetbrains.ktor.routing.routing
import java.io.File

class Main {

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            val port = System.getenv("PORT")?.toInt() ?: 8080
            embeddedServer(Netty, port) {
                routing {
                    static {
                        staticRootFolder = File("./static")
                        files("./")
                        default("index.html")
                    }
                    get("/{email}") {
                        // Since the email is part of the endpoint path, it won't be null ever
                        val email = call.parameters["email"]!!
                        val size = call.request.queryParameters["size"]
                        try {
                            val profilePictureUrl = Pikmail.getProfilePictureUrl(email, size?.toInt()).await()
                            call.respondRedirect(profilePictureUrl)
                        } catch (profileNotFountException: ProfileNotFountException) {
                            with(HttpStatusCode.NotFound) {
                                call.respondText(
                                        """{"email": "$email","status":$value,"error":"$description"}""",
                                        ContentType.Application.Json,
                                        this
                                )
                            }
                        }
                    }
                }
            }.start(wait = true)
        }

    }

}