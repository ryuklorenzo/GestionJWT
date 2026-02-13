package ies.sequeros.dam.pmdm.gestionperifl.domain.repository

interface UserRepository {
    suspend fun login(email: String, password: String): Boolean
    suspend fun register(username: String, email: String, password: String): Boolean
    suspend fun deleteAccount(password: String): Boolean
}