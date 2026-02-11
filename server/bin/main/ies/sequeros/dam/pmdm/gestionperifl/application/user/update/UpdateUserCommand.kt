package ies.sequeros.dam.pmdm.gestionperifl.application.user.update
import ies.sequeros.dam.pmdm.gestionperifl.domain.entities.User
import ies.sequeros.dam.pmdm.gestionperifl.domain.entities.UserStatus
import kotlinx.serialization.Serializable
import java.util.UUID
@Serializable
data class UpdateUserCommand (val name:String, val status: UserStatus ){

}
