package ies.sequeros.dam.pmdm.gestionperifl.domain.model

data class AuthTokens(
    val accessToken: String,
    val idToken: String,
    val refreshToken: String? = null
){}
