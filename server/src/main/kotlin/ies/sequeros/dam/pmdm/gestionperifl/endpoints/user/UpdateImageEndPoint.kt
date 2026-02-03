package ies.sequeros.dam.pmdm.gestionperifl.endpoints.user

import ies.sequeros.dam.pmdm.gestionperifl.application.exceptions.InvalidCredentialsException
import ies.sequeros.dam.pmdm.gestionperifl.application.user.getmeprofile.GetMyProfileUseCase
import ies.sequeros.dam.pmdm.gestionperifl.application.user.register.RegisterUserCommand
import ies.sequeros.dam.pmdm.gestionperifl.application.user.register.RegisterUserUseCase
import ies.sequeros.dam.pmdm.gestionperifl.application.user.updateimage.UpdateImageUserCasoDeUso
import ies.sequeros.dam.pmdm.gestionperifl.application.user.updateimage.UpdateImageUserCommand
import ies.sequeros.dam.pmdm.gestionperifl.domain.repositories.StorageEntity
import ies.sequeros.dam.pmdm.gestionperifl.ktor_config.plugins.ValidateSchema
import ies.sequeros.dam.pmdm.gestionperifl.ktor_config.processFile
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.UnsupportedMediaTypeException
import io.ktor.server.request.contentType
import io.ktor.server.request.receive
import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.header
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.koin.core.qualifier.named
import org.koin.ktor.ext.inject
import java.util.UUID
import kotlin.getValue

fun Route.updateUserImageEndPoint() {
    val updateImageUserCasoDeUso by inject<UpdateImageUserCasoDeUso>()

    val baseUrl by inject<String>(named("baseUrl"))
    route("image") {
        patch() {
            //obtener el id del tokne
            val principal = call.principal<JWTPrincipal>()
            val id = principal?.subject ?: throw InvalidCredentialsException("Error en credencial")
            val uuid = UUID.fromString(id)
            //comprobar que es un formulario
            val contentType = call.request.contentType()
            if (!contentType.match(ContentType.MultiPart.FormData)) {
                throw UnsupportedMediaTypeException(ContentType.MultiPart.FormData)
            }
            val multipart = call.receiveMultipart()
            //obtener la imagen y los bits
            val (fileName, fileBytes) = processFile(multipart)
            if (fileBytes == null) {
                throw BadRequestException("No se ha proporcionado ninguna imagen válida")
            }
            val comando = UpdateImageUserCommand(id, fileName, fileBytes)
            //modificar la imagen
            var item = updateImageUserCasoDeUso(uuid,comando)
            if(item.image != null && item.image.isNotEmpty()) {
                item=item.copy(image = baseUrl+"/" +StorageEntity.USUARIO.path+item.image)
            }
            call.respond(HttpStatusCode.OK,item)
        }
    }
}