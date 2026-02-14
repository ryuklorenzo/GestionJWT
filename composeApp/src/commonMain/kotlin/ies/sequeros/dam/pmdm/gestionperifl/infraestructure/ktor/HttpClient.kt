package ies.sequeros.dam.pmdm.gestionperifl.infraestructure.ktor

import ies.sequeros.dam.pmdm.gestionperifl.infraestructure.storage.TokenStorage
import io.ktor.client.HttpClient
import io.ktor.client.call.body

import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens

import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation

import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody

import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode

import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun createHttpClient(tokenStorage: TokenStorage,
                     refreshUrl: String
): HttpClient {
    return HttpClient {
        install(DefaultRequest) {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
        }
        //logs
        install(Logging) {

            //logger = Logger.DEFAULT
            logger = object : Logger {
                override fun log(message: String) {
                    println("KTOR CLIENT LOG: $message")
                }
            }
            level = LogLevel.ALL
        }
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            })
        }
        install(Auth) {
            bearer {
                loadTokens {
                    BearerTokens(
                        accessToken = "mi_token_jwt", refreshToken =
                            ""
                    )
                }
                refreshTokens {
                    val response =
                        client.post(refreshUrl) {
                            markAsRefreshTokenRequest()
                            //token de refresco
                            setBody(mapOf("refresh_token" to tokenStorage.getRefreshToken()))
                        }
                    if (response.status == HttpStatusCode.OK) {
                        // Leemos la respuesta directamente como un Mapa
                        val data = response.body <Map<String, String>>()
                        // Extraer los valores usando las llaves del JSON
                        val newAccess = data["access_token"] ?: ""
                        val newRefresh = data["refresh_token"] ?: "antiguo refresh token" ?: ""
                        val idToken = data["id_token"]
                        /// Será null si es OAuth, tendrá valor si es Google

                        if (idToken != null) {
                            println(
                                "Se ha recibido identidad (OIDC):$idToken")
                        }
                        BearerTokens(newAccess, newRefresh)
                    } else {
                        null
                    }
                }
            }
        }

        // Timeout
        install(HttpTimeout) {
            requestTimeoutMillis = 15000
        }
    }
}