package ies.sequeros.dam.pmdm.gestionperifl.application.usercase

import io.ktor.client.HttpClient
import io.ktor.client.request.*
import io.ktor.http.HttpStatusCode

class ChangeUserImageUseCase(
    private val client: HttpClient,
) {

    suspend operator fun invoke(userId: String, imageUrl: String): Result<Unit> {
        return try {
            val response = client.patch(
                "http://localhost:8080/api/users/$userId/image"
            ) {
                setBody(imageUrl)
            }

            when (response.status) {
                HttpStatusCode.OK, HttpStatusCode.NoContent -> Result.success(Unit)

                HttpStatusCode.Unauthorized -> Result.failure(Exception("No autorizado"))

                HttpStatusCode.NotFound -> Result.failure(Exception("Usuario no encontrado"))

                else -> Result.failure(Exception("Error inesperado: ${response.status.value}"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}