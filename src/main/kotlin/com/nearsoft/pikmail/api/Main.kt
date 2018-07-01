package com.nearsoft.pikmail.api

import com.nearsoft.ipapiklient.IpApiKlient
import com.nearsoft.ipapiklient.models.IpCheckResult
import com.nearsoft.pikmail.Pikmail
import com.nearsoft.pikmail.ProfileNotFountException
import io.ktor.application.call
import io.ktor.content.default
import io.ktor.content.files
import io.ktor.content.static
import io.ktor.content.staticRootFolder
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.response.respondRedirect
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlinx.coroutines.experimental.rx2.await
import java.io.File

class Main {

    companion object {

        private val analyticsManager = AnalyticsManager()

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
                        val ip = call.request.headers["X-Forwarded-For"]
                                ?: "RemoteHost[${call.request.local.remoteHost}]"

                        val ipCheckResult: IpCheckResult = IpApiKlient.getIpInfo(ip).await()

                        try {
                            val profilePictureUrl = Pikmail.getProfilePictureUrl(email, size?.toInt()).await()
                            call.respondRedirect(profilePictureUrl)
                            analyticsManager.trackSuccess(email, ipCheckResult, size?.toInt())
                        } catch (throwable: Throwable) {
                            if (throwable is ProfileNotFountException) {
                                with(HttpStatusCode.NotFound) {
                                    call.respondText(
                                            """{"email": "$email","status":$value,"error":"$description"}""",
                                            ContentType.Application.Json,
                                            this
                                    )
                                }
                            }
                            analyticsManager.trackError(email, ipCheckResult, throwable)
                        }
                    }
                }
            }.start(wait = true)
        }

    }

}