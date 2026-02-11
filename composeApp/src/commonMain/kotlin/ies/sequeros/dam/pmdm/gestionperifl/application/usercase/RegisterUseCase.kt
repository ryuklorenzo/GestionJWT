package ies.sequeros.dam.pmdm.gestionperifl.application.usercase

import ies.sequeros.dam.pmdm.gestionperifl.ui.register.RegisterState
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String
)


@Serializable
data class RegisterResponse(
    val id: String,
    val username: String,
    val email: String,
    val password: String,
    val image: String? = null
)
class RegisterUseCase(private val client: HttpClient) {
    suspend operator fun invoke(state: RegisterState): Result<RegisterResponse> {
        return try
        {
            val response = client.post("http://localhost:8080/api/public/register") {
                contentType(ContentType.Application.Json)
                setBody(state)
            }
            if (response.status.value in 200..299) {
                Result.success(response.body<RegisterResponse>())
            } else {
                Result.failure(Exception("Error ${response.status.value}"))
            }
        } catch (e: Exception)
        {
            Result.failure(e)
        }
    }
}