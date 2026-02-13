package ies.sequeros.dam.pmdm.gestionperifl.infraestructure.repository

import ies.sequeros.dam.pmdm.gestionperifl.application.usercase.UserProfileResponse
import ies.sequeros.dam.pmdm.gestionperifl.domain.repository.UserRepository
import ies.sequeros.dam.pmdm.gestionperifl.infraestructure.storage.TokenStorage
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.serialization.Serializable

// Definimos los DTOs aquí o impórtalos si ya los tienes en 'shared'
@Serializable
data class LoginRequest(val email: String, val password: String)

@Serializable
data class RegisterRequest(val username: String, val email: String, val password: String)

@Serializable
data class DeleteCommand(val password: String)

@Serializable
data class ChangePasswordCommand(val old_password: String, val new_password: String)

class UserRepositoryImpl(
    private val client: HttpClient,
    private val tokenStorage: TokenStorage
) : UserRepository {

    // Ajusta esta URL base según necesites (localhost para escritorio, 10.0.2.2 para emulador Android)
    private val baseUrl = "http://localhost:8080/api"

    override suspend fun login(email: String, password: String): Boolean {
        return try {
            val response = client.post("$baseUrl/public/login") {
                contentType(ContentType.Application.Json)
                setBody(LoginRequest(email, password))
            }
            response.status == HttpStatusCode.OK
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun register(username: String, email: String, password: String): Boolean {
        return try {
            val response = client.post("$baseUrl/public/register") {
                contentType(ContentType.Application.Json)
                setBody(RegisterRequest(username, email, password))
            }
            response.status == HttpStatusCode.Created || response.status == HttpStatusCode.OK
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun deleteAccount(password: String): Boolean {
        return try {
            val token = tokenStorage.getAccessToken()
            if (token.isNullOrBlank()) {
                println("UserRepositoryImpl: No hay token disponible para borrar la cuenta.")
                return false
            }

            val response = client.delete("http://localhost:8080/api/users/me") {
                contentType(ContentType.Application.Json)
                bearerAuth(token)
                setBody(DeleteCommand(password = password))
            }

            println("UserRepositoryImpl: Delete status code = ${response.status}")

            response.status == HttpStatusCode.NoContent || response.status == HttpStatusCode.OK
        } catch (e: Exception) {
            println("UserRepositoryImpl: Excepción al borrar cuenta: ${e.message}")
            e.printStackTrace()
            false
        }
    }

    override suspend fun getProfile(): Result<UserProfileResponse> {
        return try {
            val token = tokenStorage.getAccessToken() ?: return Result.failure(Exception("No token"))
            val response = client.get("$baseUrl/users/me") {
                bearerAuth(token)
            }
            if (response.status == HttpStatusCode.OK) {
                Result.success(response.body())
            } else {
                Result.failure(Exception("Error al obtener perfil: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    override suspend fun changePassword(oldPass: String, newPass: String): Boolean {
        try {
            val token = tokenStorage.getAccessToken() ?: throw Exception("No hay sesión activa")

            val response = client.put("$baseUrl/users/me/password") {
                contentType(ContentType.Application.Json)
                bearerAuth(token)
                setBody(ChangePasswordCommand(old_password = oldPass, new_password = newPass))
            }

            if (response.status == HttpStatusCode.OK || response.status == HttpStatusCode.NoContent) {
                return true
            } else {
                // Aquí capturamos el mensaje exacto del servidor ("Credenciales incorrectas" o "Token inválido")
                val errorMsg = response.bodyAsText()
                throw Exception(errorMsg)
            }
        } catch (e: Exception) {
            // Re-lanzamos la excepción para que llegue al UseCase
            throw e
        }
    }
}