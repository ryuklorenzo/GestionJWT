package ies.sequeros.dam.pmdm.gestionperifl.application.usercase

import kotlinx.serialization.Serializable

@Serializable
data class UserProfileResponse(
    val id: String,
    val username: String,
    val email: String
)