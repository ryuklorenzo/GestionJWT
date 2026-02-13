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
data class ChangePasswordCommand(val oldPassword: String, val newPassword: String)

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
            // Aquí deberías guardar el token si la respuesta devuelve uno
            // Ejemplo: val tokens: AuthTokens = response.body()
            // tokenStorage.saveAccessToken(tokens.accessToken)
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
            // 1. Obtener el token
            val token = tokenStorage.getAccessToken()
            if (token.isNullOrBlank()) {
                println("UserRepositoryImpl: No hay token disponible para borrar la cuenta.")
                return false
            }

            // 2. Hacer la petición con la ruta correcta y autorización
            // Nota: Usamos la ruta completa que confirmaste que funciona
            val response = client.delete("http://localhost:8080/api/users/me") {
                contentType(ContentType.Application.Json)
                bearerAuth(token) // <--- ESTO SOLUCIONA EL 401 UNAUTHORIZED
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
            val response = client.get("$baseUrl/users/me") { // Asegúrate si es 'users' o 'user' aquí también
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
        return try {
            val token = tokenStorage.getAccessToken()
            if (token.isNullOrBlank()) {
                println("UserRepositoryImpl: No hay token disponible para actualizar la contraseña.")
                return false
            }
            val response = client.put("http://localhost:8080/api/users/me/password") {
                contentType(ContentType.Application.Json)
                bearerAuth(token)
                setBody(ChangePasswordCommand(oldPassword = oldPass, newPassword = newPass))
            }
            println("UserRepositoryImpl: Delete status code = ${response.status}")
            response.status == HttpStatusCode.NoContent || response.status == HttpStatusCode.OK
        }
        catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}