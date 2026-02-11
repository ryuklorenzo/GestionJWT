package ies.sequeros.dam.pmdm.gestionperifl.application.commons

import ies.sequeros.dam.pmdm.gestionperifl.application.serializers.UUIDSerializer
import ies.sequeros.dam.pmdm.gestionperifl.domain.entities.User
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class UserDto(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val username: String, val email: String, val image: String? = null, val status: String
) {
    companion object {
        fun fromDomain(user: User): UserDto {
            return UserDto(user.id, username = user.name, email = user.email, user.image ?: null,user.status.name)

        }
    }
}
