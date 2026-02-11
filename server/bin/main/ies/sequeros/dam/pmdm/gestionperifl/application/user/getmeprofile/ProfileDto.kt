package ies.sequeros.dam.pmdm.gestionperifl.application.user.getmeprofile

import ies.sequeros.dam.pmdm.gestionperifl.application.serializers.UUIDSerializer
import ies.sequeros.dam.pmdm.gestionperifl.domain.entities.UserStatus
import kotlinx.serialization.Serializable
import java.util.UUID
@Serializable
data class ProfileDto (
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val name:String,
    val email:String,
    val image:String?=null,
    val status: UserStatus ) {

}