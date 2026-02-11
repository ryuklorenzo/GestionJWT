package ies.sequeros.dam.pmdm.gestionperifl.application.usercase

import ies.sequeros.dam.pmdm.gestionperifl.domain.repository.UserRepository
import kotlinx.serialization.Serializable

// Tus DTOs se mantienen aquí como pediste
@Serializable
data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String
)

@Serializable
data class RegisterResponse(
    val id: Long, // Ajustado a Long si tu DB es autoincrement numérico, o String si es UUID
    val username: String,
    val email: String,
    val image: String? = null
)

// El caso de uso recibe el Repositorio, NO el HttpClient
class RegisterUseCase(private val repository: UserRepository) {

    suspend operator fun invoke(request: RegisterRequest): Result<RegisterResponse> {
        return repository.register(request)
    }
}