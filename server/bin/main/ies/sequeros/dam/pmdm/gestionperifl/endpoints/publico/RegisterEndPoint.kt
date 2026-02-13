package ies.sequeros.dam.pmdm.gestionperifl.endpoints.publico

import ies.sequeros.dam.pmdm.gestionperifl.application.user.register.RegisterUserCommand
import ies.sequeros.dam.pmdm.gestionperifl.application.user.register.RegisterUserUseCase
import ies.sequeros.dam.pmdm.gestionperifl.ktor_config.plugins.ValidateSchema
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.header
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject
import kotlin.getValue

fun Route.registerEndPoint() {
    val registerUseCase by inject<RegisterUserUseCase>()
    route("register") {
        /*install(ValidateSchema) {
            schemaPath = "json_schemas/register-user-command.schema.json"
        }*/
        post() {

            val command = call.receive<RegisterUserCommand>()
            var item = registerUseCase(command)
            //solo es accesible si se ha logeado
            val location = "/api/users/me"
            call.response.header(HttpHeaders.Location, location)
            call.respond(HttpStatusCode.Created, item)
        }
    }
}