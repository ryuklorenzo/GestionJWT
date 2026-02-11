package ies.sequeros.dam.pmdm.gestionperifl.domain.repository

import ies.sequeros.dam.pmdm.gestionperifl.application.usercase.RegisterRequest
import ies.sequeros.dam.pmdm.gestionperifl.application.usercase.RegisterResponse

interface UserRepository {
    // Definimos el método de registro devolviendo Result con el Response
    suspend fun register(request: RegisterRequest): Result<RegisterResponse>

    // Mantén aquí tu método de login si lo tienes
    suspend fun login(email: String, password: String): Boolean
}