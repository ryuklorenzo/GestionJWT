package ies.sequeros.dam.pmdm.gestionperifl.application.user.register
import ies.sequeros.dam.pmdm.gestionperifl.domain.entities.User
import kotlinx.serialization.Serializable


import java.util.UUID
@Serializable
data class RegisterUserCommand (val email:String,val username: String, val password: String ){

}
fun RegisterUserCommand.toDomain(id: UUID):User{
    return User(id,username,email)
}