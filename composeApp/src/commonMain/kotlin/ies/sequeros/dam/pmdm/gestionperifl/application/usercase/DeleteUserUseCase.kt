package ies.sequeros.dam.pmdm.gestionperifl.application.usercase

import ies.sequeros.dam.pmdm.gestionperifl.domain.repository.UserRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.http.HttpStatusCode

class DeleteUserUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(password: String): Result<Boolean> {
        return try {
            val result = repository.deleteAccount(password)
            if (result) Result.success(true) else Result.failure(Exception("Error al borrar"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}