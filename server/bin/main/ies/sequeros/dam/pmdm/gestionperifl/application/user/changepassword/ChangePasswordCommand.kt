package ies.sequeros.dam.pmdm.gestionperifl.application.user.changepassword
import ies.sequeros.dam.pmdm.gestionperifl.domain.entities.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID
@Serializable
data class ChangePasswordCommand (
    @SerialName("old_password")
    val oldPassword:String,
    @SerialName("new_password")
    val newPassword: String ){

}
