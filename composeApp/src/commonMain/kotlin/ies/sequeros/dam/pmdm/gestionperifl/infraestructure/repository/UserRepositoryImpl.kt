package ies.sequeros.dam.pmdm.gestionperifl.infraestructure.repository

import ies.sequeros.dam.pmdm.gestionperifl.application.usercase.LoginRequest
import ies.sequeros.dam.pmdm.gestionperifl.application.usercase.LoginResponse
import ies.sequeros.dam.pmdm.gestionperifl.application.usercase.RegisterRequest
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
    private val tokenStorage: TokenStorage
) : UserRepository {

    // CORREGIDO: localhost para Desktop
    private val baseUrl = "http://localhost:8080/api/public"

    override suspend fun login(email: String, password: String): Boolean {
        // ... (igual que antes)
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
            // 201 Created indica éxito
            response.status == HttpStatusCode.Created
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}