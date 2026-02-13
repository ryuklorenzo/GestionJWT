package ies.sequeros.dam.pmdm.gestionperifl.endpoints.publico

import ies.sequeros.dam.pmdm.gestionperifl.application.user.login.LoginUserCommand
import ies.sequeros.dam.pmdm.gestionperifl.application.user.login.LoginUserUseCase
import ies.sequeros.dam.pmdm.gestionperifl.ktor_config.plugins.ValidateSchema
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject
import kotlin.getValue

fun Route.loginEndPoint() {
    val loginUserUseCase by inject<LoginUserUseCase>()
    route("login") {

       /* install(ValidateSchema) {
            schemaPath = "json_schemas/login-user-command.schema.json"
        }*/
        post() {

            val command = call.receive<LoginUserCommand>()
            var loginDto = loginUserUseCase(command)
            call.respond(HttpStatusCode.OK, loginDto)

        }
    }
}