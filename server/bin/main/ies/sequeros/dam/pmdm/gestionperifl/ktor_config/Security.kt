package ies.sequeros.dam.pmdm.gestionperifl.ktor_config

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.auth.authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.response.respond

fun Application.configureSecurity() {
    // Extracción de parámetros para Usuario
    val userSecret = environment.config.property("jwt.secret").getString()
    val userIssuer = environment.config.property("jwt.issuer").getString()
    val userRealm = environment.config.property("jwt.realm").getString()

    authentication {
        jwt("auth-user") {
            realm = userRealm
            verifier(
                JWT.require(Algorithm.HMAC256(userSecret))
                    .withIssuer(userIssuer)
                    .build()
            )
            //no se hacen comprobacione
            validate { credential ->
                //tiene que tener el claim subject
                if (credential.payload.subject != null) {
                    //se pasa la carga útil a los end points
                    JWTPrincipal(credential.payload)
                } else {
                    null //  autenticación falla
                }
            }
            //devuelve no autorizado
            challenge { defaultScheme, realm ->
                call.respond(HttpStatusCode.Unauthorized,"Token no válido")          }
        }
    }
}
