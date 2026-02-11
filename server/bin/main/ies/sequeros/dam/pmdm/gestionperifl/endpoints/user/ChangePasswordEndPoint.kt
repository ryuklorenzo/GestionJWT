package ies.sequeros.dam.pmdm.gestionperifl.endpoints.user

import ies.sequeros.dam.pmdm.gestionperifl.application.exceptions.InvalidCredentialsException
import ies.sequeros.dam.pmdm.gestionperifl.application.user.changepassword.ChangePasswordCommand
import ies.sequeros.dam.pmdm.gestionperifl.application.user.changepassword.ChangePasswordUseCase
import ies.sequeros.dam.pmdm.gestionperifl.application.user.getmeprofile.GetMyProfileUseCase
import ies.sequeros.dam.pmdm.gestionperifl.application.user.register.RegisterUserCommand
import ies.sequeros.dam.pmdm.gestionperifl.application.user.register.RegisterUserUseCase
import ies.sequeros.dam.pmdm.gestionperifl.domain.repositories.StorageEntity
import ies.sequeros.dam.pmdm.gestionperifl.ktor_config.plugins.ValidateSchema
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.header
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import org.koin.core.qualifier.named
import org.koin.ktor.ext.inject
import java.util.UUID
import kotlin.getValue
import kotlin.invoke

fun Route.changePasswordEndPoint() {
    val changePasswordUseCase by inject<ChangePasswordUseCase>()

    val baseUrl by inject<String>(named("baseUrl"))
    route("password") {
        /*install(ValidateSchema) {
            schemaPath = "json_schemas/change-password-command.schema.json"
        }*/
        put() {

            val command = call.receive<ChangePasswordCommand>()
            val principal = call.principal<JWTPrincipal>()
            val id = principal?.subject ?: throw InvalidCredentialsException("Error en credencial")
            val uuid = UUID.fromString(id)
            changePasswordUseCase.invoke(uuid, command)
            call.respond(HttpStatusCode.NoContent)
        }
    }
}