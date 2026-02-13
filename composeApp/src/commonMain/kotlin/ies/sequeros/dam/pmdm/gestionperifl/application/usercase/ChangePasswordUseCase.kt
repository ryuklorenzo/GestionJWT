package ies.sequeros.dam.pmdm.gestionperifl.application.usercase

import ies.sequeros.dam.pmdm.gestionperifl.domain.repository.UserRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.serialization.Serializable

@Serializable
data class ChangePasswordRequest(val oldPassword: String, val newPassword: String)

class ChangePasswordUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(oldPass: String, newPass: String): Result<Boolean> {
        return try {
            // Ahora repository.changePassword lanzará excepción con el mensaje del servidor si falla
            val result = repository.changePassword(oldPass, newPass)
            Result.success(result)
        } catch (e: Exception) {
            // Capturamos la excepción (con el mensaje "Contraseña incorrecta") y la devolvemos
            Result.failure(e)
        }
    }
}