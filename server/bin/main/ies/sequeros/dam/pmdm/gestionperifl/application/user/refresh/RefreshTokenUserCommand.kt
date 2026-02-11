package ies.sequeros.dam.pmdm.gestionperifl.application.user.refresh
import ies.sequeros.dam.pmdm.gestionperifl.domain.entities.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID
@Serializable
data class RefreshTokenUserCommand (
    @SerialName("refresh_token")
    val refreshToken:String ){

}
