package ies.sequeros.dam.pmdm.gestionperifl.application.usercase

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.Serializable
import okio.EOFException

@Serializable
data class UserProfileRespone(
    val id: String,
    val username: String,
    val email: String,
    val createdAt: String
)

class GetProfileUseCase(private val client: HttpClient) {
    suspend operator fun invoke(): Result<UserProfileRespone> {
        return try {
            val response = client.get("http://10.0.2.2:8080/api/users/me")

            when (response.status) {
                HttpStatusCode.OK -> {
                    Result.success(response.body<UserProfileRespone>())
                }
                HttpStatusCode.Unauthorized -> {
                    Result.failure(Exception("Sesión expirada. Por favor, inicia sesión de nuevo."))
                }
                else -> {
                    Result.failure(Exception("Error al obtener perfil: ${response.status.value}"))
                }
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}