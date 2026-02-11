package ies.sequeros.dam.pmdm.gestionperifl.application.user.delete
import ies.sequeros.dam.pmdm.gestionperifl.domain.entities.User
import kotlinx.serialization.Serializable
import java.util.UUID
@Serializable
data class DeleteCommand (val password:String){

}
