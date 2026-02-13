package ies.sequeros.dam.pmdm.gestionperifl.endpoints.user

import ies.sequeros.dam.pmdm.gestionperifl.application.exceptions.InvalidCredentialsException
import ies.sequeros.dam.pmdm.gestionperifl.application.user.changepassword.ChangePasswordCommand
import ies.sequeros.dam.pmdm.gestionperifl.application.user.changepassword.ChangePasswordUseCase
import ies.sequeros.dam.pmdm.gestionperifl.application.user.getmeprofile.GetMyProfileUseCase
import ies.sequeros.dam.pmdm.gestionperifl.application.user.register.RegisterUserCommand
import ies.sequeros.dam.pmdm.gestionperifl.application.user.register.RegisterUserUseCase
import ies.sequeros.dam.pmdm.gestionperifl.application.user.update.UpdateUserCasoDeUso
import ies.sequeros.dam.pmdm.gestionperifl.application.user.update.UpdateUserCommand
import ies.sequeros.dam.pmdm.gestionperifl.domain.repositories.StorageEntity
import ies.sequeros.dam.pmdm.gestionperifl.ktor_config.plugins.ValidateSchema
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.header
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.method
import io.ktor.server.routing.patch
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import org.koin.core.qualifier.named
import org.koin.ktor.ext.inject
import java.util.UUID
import kotlin.getValue
import kotlin.invoke

fun Route.updateUserEndPoint() {
    val baseUrl by inject<String>(named("baseUrl"))

    val updateUserUseCase by inject<UpdateUserCasoDeUso>()

    // Creamos un subnodo para el PATCH
    method(HttpMethod.Patch) {
       /* install(ValidateSchema) {
            schemaPath = "json_schemas/update-user-command.schema.json"
        }*/
        handle {
            val command = call.receive<UpdateUserCommand>()
            val principal = call.principal<JWTPrincipal>()
            val id = principal?.subject ?: throw InvalidCredentialsException("Error en credencial")
            val uuid = UUID.fromString(id)
            var user= updateUserUseCase.invoke(uuid, command)
            if(user.image != null && user.image.isNotEmpty()) {
                user=user.copy(image = baseUrl+"/" +StorageEntity.USUARIO.path+user.image)
            }
            call.respond(HttpStatusCode.OK,user)
        }
    }


}