package ies.sequeros.dam.pmdm.gestionperifl.application.user.changepassword

import ies.sequeros.dam.pmdm.gestionperifl.application.exceptions.BusinessException
import ies.sequeros.dam.pmdm.gestionperifl.application.exceptions.InvalidCredentialsException
import ies.sequeros.dam.pmdm.gestionperifl.application.exceptions.NotFoundException
import ies.sequeros.dam.pmdm.gestionperifl.application.services.ITokenService
import ies.sequeros.dam.pmdm.gestionperifl.application.user.refresh.RefreshDto
import ies.sequeros.dam.pmdm.gestionperifl.application.user.refresh.RefreshTokenUserCommand
import ies.sequeros.dam.pmdm.gestionperifl.domain.repositories.IUserRepository
import ies.sequeros.dam.pmdm.gestionperifl.domain.services.IPasswordEncoder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

class ChangePasswordUseCase(
    val repository: IUserRepository,
    val passwordEnconder: IPasswordEncoder,

) {
    suspend operator fun invoke(id: UUID, command: ChangePasswordCommand): Unit =
        withContext(Dispatchers.IO) {
            //obtener el password antiguo
            val oldPassword=repository.getPasswordHash(id)?:throw InvalidCredentialsException("User")
            //comprobar si el que llega nuevo es diferente al que ya existe
            if(passwordEnconder.matches(command.newPassword,oldPassword)){
                  throw BusinessException("La nueva clave ha de ser diferente a la anterior")
            }
            //comprobar que el que llega antiguo es igual al que esta almacenado
            val match=passwordEnconder.matches(command.oldPassword,oldPassword)
            if(!match){
                throw  InvalidCredentialsException("User")
            }
            //codificar
            val newPassword=passwordEnconder.encode(command.newPassword)
            //guardar
            repository.updatePassword(id,newPassword)
        }
}