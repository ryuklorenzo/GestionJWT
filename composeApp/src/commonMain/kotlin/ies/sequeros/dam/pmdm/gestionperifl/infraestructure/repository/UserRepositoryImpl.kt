package ies.sequeros.dam.pmdm.gestionperifl.infraestructure.repository

import ies.sequeros.dam.pmdm.gestionperifl.application.usercase.LoginRequest
import ies.sequeros.dam.pmdm.gestionperifl.application.usercase.LoginResponse
import ies.sequeros.dam.pmdm.gestionperifl.application.usercase.RegisterRequest
import ies.sequeros.dam.pmdm.gestionperifl.application.usercase.RegisterResponse
import ies.sequeros.dam.pmdm.gestionperifl.domain.repository.UserRepository
import ies.sequeros.dam.pmdm.gestionperifl.infraestructure.storage.TokenStorage
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class UserRepositoryImpl(
    private val client: HttpClient,
    private val tokenStorage: TokenStorage // Para guardar el token al hacer login
) : UserRepository {

    // IMPORTANTE:
    // Si usas Emulador Android: http://10.0.2.2:8080/api/public
    // Si usas Desktop/Web: http://localhost:8080/api/public
    private val baseUrl = "http://10.0.2.2:8080/api/public"

    override suspend fun login(email: String, password: String): Boolean {
        return try {
            val response = client.post("$baseUrl/login") {
                contentType(ContentType.Application.Json)
                setBody(LoginRequest(email, password))
            }

            if (response.status == HttpStatusCode.OK) {
                // Login exitoso: Guardamos el token
                val tokens = response.body<LoginResponse>()
                tokenStorage.saveAccessToken(tokens.accessToken)
                // tokenStorage.saveRefreshToken(tokens.refreshToken) // Si lo usas
                true
            } else {
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun register(username: String, email: String, password: String): Boolean {
        return try {
            val response = client.post("$baseUrl/register") {
                contentType(ContentType.Application.Json)
                setBody(RegisterRequest(username = username, email = email, password = password))
            }
            // Si devuelve 201 Created, el registro fue exitoso
            response.status == HttpStatusCode.Created
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}