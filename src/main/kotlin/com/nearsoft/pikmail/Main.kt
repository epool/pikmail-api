package com.nearsoft.pikmail

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
                        val email = call.parameters["email"]
                        val size = call.request.queryParameters["size"]
                        if (email != null) {
                            val profilePictureUrl = getProfilePictureUrl(email, size)
                            if (profilePictureUrl != null) {
                                call.respondRedirect(profilePictureUrl)
                            } else {
                                val notFoundStatusCode = HttpStatusCode.NotFound
                                call.respondText("{\"status\": ${notFoundStatusCode.value}, \"error\": \"${notFoundStatusCode.description}\"}", ContentType.Application.Json, notFoundStatusCode)
                            }
                        } else {
                            call.respondRedirect("/")
                        }
                    }
                }
            }.start(wait = true)
        }

        private fun getProfilePictureUrl(email: String, size: String?) = try {
            Pikmail.getProfilePictureUrl(email, size?.toInt()).blockingGet()
        } catch (e: Exception) {
            null
        }

    }

}