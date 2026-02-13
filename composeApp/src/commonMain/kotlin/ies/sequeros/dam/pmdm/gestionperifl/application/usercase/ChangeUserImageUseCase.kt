package ies.sequeros.dam.pmdm.gestionperifl.application.usercase

import coil3.Uri
import io.ktor.client.HttpClient
import io.ktor.client.request.*
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.hooks.ResponseBodyReadyForSend

class ChangeUserImageUseCase(
    private val client: HttpClient
) {

    suspend operator fun invoke(
        userId: String,
        imageBytes: ByteArray,
        fileName: String = "profile.jpg"
    ): Result<Unit> {

        return try {
            val response = client.patch(
                "http://10.0.2.2:8080/api/users/$userId/image"
            ) {
                setBody(
                    MultiPartFormDataContent(
                        formData {
                            append(
                                "file",
                                imageBytes,
                                Headers.build {
                                    append(HttpHeaders.ContentType, "image/jpeg")
                                    append(
                                        HttpHeaders.ContentDisposition,
                                        "filename=\"$fileName\""
                                    )
                                }
                            )
                        }
                    )
                )
            }

            when (response.status) {
                HttpStatusCode.OK -> Result.success(Unit)
                HttpStatusCode.Unauthorized -> Result.failure(Exception("No autorizado"))
                HttpStatusCode.NotFound -> Result.failure(Exception("Usuario no encontrado"))
                else -> Result.failure(Exception("Error ${response.status.value}"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
