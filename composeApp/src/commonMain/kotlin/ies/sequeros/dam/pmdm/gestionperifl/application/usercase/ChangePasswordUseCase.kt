package ies.sequeros.dam.pmdm.gestionperifl.application.usercase

import io.ktor.client.HttpClient
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.serialization.Serializable

@Serializable
data class ChangePasswordRequest(val oldPassword: String, val newPassword: String)

class ChangePasswordUseCase(private val client: HttpClient) {

    // CAMBIO 1: Cambiado Result<String> a Result<Unit> porque devuelves Unit al tener éxito
    suspend operator fun invoke(oldPass: String, newPass: String): Result<Unit> {
        // CAMBIO 2: Añadido 'return' aquí para devolver el resultado del bloque
        return try {
            // Nota: 10.0.2.2 funciona para el Emulador de Android.
            // Si usas Desktop, deberías usar "localhost".
            val response = client.put("http://10.0.2.2:8080/api/users/me/password") {
                contentType(ContentType.Application.Json)
                setBody(ChangePasswordRequest(oldPassword = oldPass, newPassword = newPass))
            }

            when (response.status) {
                HttpStatusCode.OK, HttpStatusCode.NoContent -> {
                    Result.success(Unit)
                }
                HttpStatusCode.BadRequest -> {
                    // CAMBIO 3: Corregido 'failture' -> 'failure'
                    Result.failure(Exception("La contraseña antigua no es correcta o la nueva no cumple los requisitos"))
                }
                HttpStatusCode.Unauthorized -> {
                    Result.failure(Exception("Sesión no válida"))
                }
                else -> {
                    Result.failure(Exception("Error al cambiar contraseña: ${response.status.value}"))
                }
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}