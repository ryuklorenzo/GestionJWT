package ies.sequeros.dam.pmdm.gestionperifl.endpoints.user

import ies.sequeros.dam.pmdm.gestionperifl.application.exceptions.InvalidCredentialsException
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
import io.ktor.server.routing.route
import org.koin.core.qualifier.named
import org.koin.ktor.ext.inject
import java.util.UUID
import kotlin.getValue

fun Route.profileEndPoint() {
    val getMyProfileUseCase by inject<GetMyProfileUseCase>()
    val baseUrl by inject<String>(named("baseUrl"))
    route("") {
        get("") {
            //se accede a los valores del token donde el subject es el id del usuario
            val principal = call.principal<JWTPrincipal>()
            val id = principal?.subject ?: throw InvalidCredentialsException("Error en credencial")
            var profile = getMyProfileUseCase(UUID.fromString(id))
            if(profile.image != null && profile.image.isNotEmpty()) {
                profile=profile.copy(image = baseUrl+"/" +StorageEntity.USUARIO.path+profile.image)
            }

            call.respond(profile)
        }
    }
}