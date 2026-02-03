package ies.sequeros.dam.pmdm.gestionperifl.application.services

import ies.sequeros.dam.pmdm.gestionperifl.domain.entities.User
import java.util.UUID

data class AuthTokens(
    val accessToken: String,
    val idToken: String,
    val refreshToken: String,
    val expiresIn: Long
)
interface ITokenService {
    fun generateAuthTokens(user: User): AuthTokens
    fun validateToken(token: String): UUID
}