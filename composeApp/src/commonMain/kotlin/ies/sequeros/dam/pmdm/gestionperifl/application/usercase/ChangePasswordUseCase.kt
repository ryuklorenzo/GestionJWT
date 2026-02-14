package ies.sequeros.dam.pmdm.gestionperifl.application.usercase

import io.ktor.client.HttpClient
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.serialization.Serializable

@Serializable
data class ChangePasswordRequest(val old_password: String, val new_password: String)

class ChangePasswordUseCase(private val client: HttpClient) {

    suspend operator fun invoke(oldPass: String, newPass: String): Result<Unit> {

        return try {
            val response = client.put("http://localhost:8080/api/users/me/password") {
                contentType(ContentType.Application.Json)
                setBody(ChangePasswordRequest(old_password = oldPass, new_password = newPass))
            }

            when (response.status) {
                HttpStatusCode.OK, HttpStatusCode.NoContent -> {
                    Result.success(Unit)
                }
                HttpStatusCode.BadRequest -> {
                    Result.failure(Exception("La contraseña antigua no es correcta o la nueva no cumple los requisitos"))
                }
                HttpStatusCode.Unauthorized -> {
                    Result.failure(Exception("Sesión no válida"))
                }
                else -> {
                    Result.failure(Exception("Error al cambiar contraseña: ${response.status.value}"))
                }
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}