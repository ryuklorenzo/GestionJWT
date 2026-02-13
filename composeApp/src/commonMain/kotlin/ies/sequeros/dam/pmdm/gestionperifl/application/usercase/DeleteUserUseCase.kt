package ies.sequeros.dam.pmdm.gestionperifl.application.usercase

import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.http.HttpStatusCode

class DeleteUserUseCase(private val client: HttpClient) {

    suspend operator fun invoke(userId: String): Result<Unit> {
        return try {
            val response = client.delete("http://localhost:8080/api/users/$userId")

            when (response.status) {
                HttpStatusCode.NoContent, HttpStatusCode.OK -> {
                    Result.success(Unit)
                }
                HttpStatusCode.Unauthorized -> {
                    Result.failure(Exception("No autorizado: La sesión ha expirado o el token es inválido"))
                }
                HttpStatusCode.Forbidden -> {
                    Result.failure(Exception("No tienes permisos para borrar este usuario"))
                }
                HttpStatusCode.NotFound -> {
                    Result.failure(Exception("El usuario con ID $userId no existe"))
                }
                else -> {
                    Result.failure(Exception("Error inesperado: ${response.status.value}"))
                }
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}