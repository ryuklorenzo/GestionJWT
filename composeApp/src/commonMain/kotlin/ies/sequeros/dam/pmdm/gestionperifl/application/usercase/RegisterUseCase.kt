package ies.sequeros.dam.pmdm.gestionperifl.application.usercase

import ies.sequeros.dam.pmdm.gestionperifl.domain.repository.UserRepository
import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String
)

@Serializable
data class RegisterResponse(
    val id: String,
    val username: String,
    val email: String,
    val password: String,
    val image: String? = null
)

// CORREGIDO: Recibe UserRepository
class RegisterUseCase(private val repository: UserRepository) {

    suspend operator fun invoke(username: String, email: String, password: String): Boolean {
        return repository.register(username, email, password)
    }
}