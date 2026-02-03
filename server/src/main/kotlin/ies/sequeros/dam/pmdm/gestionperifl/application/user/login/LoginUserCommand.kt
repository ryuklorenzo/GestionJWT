package ies.sequeros.dam.pmdm.gestionperifl.application.user.login
import ies.sequeros.dam.pmdm.gestionperifl.domain.entities.User
import kotlinx.serialization.Serializable
import java.util.UUID
@Serializable
data class LoginUserCommand (val email:String,
                             val password: String ){

}
