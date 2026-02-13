package ies.sequeros.dam.pmdm.gestionperifl.application.usercase

import ies.sequeros.dam.pmdm.gestionperifl.ui.components.login.LoginState
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
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

    suspend operator fun invoke(state: LoginState): Result<LoginResponse> {
        return try {
            val request = LoginRequest(
                email = state.email,
                password = state.password
            )

            val response = client.post("http://localhost:8080/api/public/login") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }

            if (response.status.isSuccess()) {
                Result.success(response.body<LoginResponse>())
            } else if (response.status.value == 401) {
                Result.failure(Exception("Credenciales incorrectas"))
            } else {
                Result.failure(Exception("Error del servidor: ${response.status.value}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}