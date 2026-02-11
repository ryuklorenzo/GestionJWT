package ies.sequeros.dam.pmdm.gestionperifl.application.usercase

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)


@Serializable
data class LoginResponse(
    val access_token: String,
    val id_token: String,
    val expires_in: Long,
    val refresh_token: String? = null
)

class LoginUseCase(private val client: HttpClient) {

    suspend operator fun invoke(command: ies.sequeros.dam.pmdm.gestionperifl.ui.components.login.LoginState): Result<LoginResponse>{
        return try {
            val response = client.post("http://localhost:8080/api/public/login") {
                contentType(ContentType.Application.Json)
                setBody(command)
            }
            // Si la respuesta es 200 OK, devolvemos éxito
            if (response.status.value in 200..299) {
                Result.success(response.body<LoginResponse>())
            } else {
                Result.failure(Exception("Error ${response.status.value}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}