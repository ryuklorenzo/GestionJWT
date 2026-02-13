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
            // Si el repo lanza excepción (ej: "Credenciales incorrectas"), cae en el catch
            val success = repository.changePassword(oldPass, newPass)
            Result.success(success)
        } catch (e: Exception) {
            // Devolvemos el error encapsulado
            Result.failure(e)
        }
    }
}