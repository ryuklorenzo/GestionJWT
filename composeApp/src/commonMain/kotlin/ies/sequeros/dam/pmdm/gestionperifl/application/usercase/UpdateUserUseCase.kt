package ies.sequeros.dam.pmdm.gestionperifl.application.usercase

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserCommand(
    val username: String,
    val email: String
)

class UpdateUserUseCase(private val client: HttpClient) {

    suspend operator fun invoke(command: UpdateUserCommand): Result<UserProfileResponse> {
        return try {

            val response = client.put("http://10.0.2.2:8080/api/users/me") {
                contentType(ContentType.Application.Json)
                setBody(command)
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    Result.success(response.body<UserProfileResponse>())
                }
                HttpStatusCode.BadRequest -> {
                    Result.failure(Exception("Datos de actualización no válidos o email ya en uso"))
                }
                HttpStatusCode.Unauthorized -> {
                    Result.failure(Exception("No autorizado: la sesión ha expirado"))
                }
                else -> {
                    Result.failure(Exception("Error al actualizar: ${response.status.value}"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}