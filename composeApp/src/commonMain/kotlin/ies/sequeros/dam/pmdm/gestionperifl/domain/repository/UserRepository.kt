package ies.sequeros.dam.pmdm.gestionperifl.domain.repository

import ies.sequeros.dam.pmdm.gestionperifl.application.usercase.UserProfileResponse

interface UserRepository {
    suspend fun login(email: String, password: String): Boolean
    suspend fun register(username: String, email: String, password: String): Boolean
    suspend fun deleteAccount(password: String): Boolean
    suspend fun getProfile(): Result<UserProfileResponse>
    suspend fun changePassword(oldPass: String, newPass: String): Boolean

}