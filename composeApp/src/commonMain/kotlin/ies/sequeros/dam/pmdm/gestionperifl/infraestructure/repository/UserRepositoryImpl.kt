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
import io.ktor.http.isSuccess

class UserRepositoryImpl(
    private val client: HttpClient,
    private val tokenStorage: TokenStorage
) : UserRepository {

    // Ajusta la IP según tu entorno (10.0.2.2 para emulador Android)
    private val baseUrl = "http://localhost:8080/api/public"

    override suspend fun register(request: RegisterRequest): Result<RegisterResponse> {
        return try {
            val response = client.post("$baseUrl/register") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }

            if (response.status.isSuccess()) {
                // Ktor deserializa el JSON de respuesta a RegisterResponse
                val responseBody = response.body<RegisterResponse>()
                Result.success(responseBody)
            } else {
                // Puedes intentar leer el mensaje de error del servidor si envía uno
                Result.failure(Exception("Error en el registro: ${response.status}"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun login(email: String, password: String): Boolean {
        // ... (Tu implementación de login existente) ...
        return try {
            val response = client.post("$baseUrl/login") {
                contentType(ContentType.Application.Json)
                setBody(LoginRequest(email, password))
            }
            if (response.status == HttpStatusCode.OK) {
                val tokens = response.body<LoginResponse>()
                //tokenStorage.saveAccessToken(tokens.accessToken)
                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }
}