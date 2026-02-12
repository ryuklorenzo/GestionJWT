package ies.sequeros.dam.pmdm.gestionperifl.domain.model

data class AuthTokens(
    val accessToken: String,
    val refreshToken: String?,
    val idToken: String?
){}
