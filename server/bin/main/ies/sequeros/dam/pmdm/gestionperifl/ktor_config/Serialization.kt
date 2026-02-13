package ies.sequeros.dam.pmdm.gestionperifl.ktor_config

import io.ktor.serialization.kotlinx.json.*
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.*

fun Application.configureSerialization() {
    val install = install(ContentNegotiation) {
        json()
    }
}
