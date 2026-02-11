package ies.sequeros.dam.pmdm.gestionperifl.application.user.update

import ies.sequeros.dam.pmdm.gestionperifl.application.commons.UserDto
import ies.sequeros.dam.pmdm.gestionperifl.application.exceptions.AlreadyExistsException
import ies.sequeros.dam.pmdm.gestionperifl.application.exceptions.NotFoundException
import ies.sequeros.dam.pmdm.gestionperifl.application.user.updateimage.UpdateImageUserCommand
import ies.sequeros.dam.pmdm.gestionperifl.domain.entities.UserStatus
import ies.sequeros.dam.pmdm.gestionperifl.domain.repositories.IFilesRepository
import ies.sequeros.dam.pmdm.gestionperifl.domain.repositories.IUserRepository
import ies.sequeros.dam.pmdm.gestionperifl.domain.repositories.StorageEntity

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.hibernate.exception.ConstraintViolationException
import java.util.UUID

class UpdateUserCasoDeUso(
   private val repository: IUserRepository,

) {
    suspend operator fun invoke(id:UUID,command: UpdateUserCommand): UserDto =
        withContext(Dispatchers.IO) {
            //obtener el usuario
            var user = repository.findById(id) ?: throw NotFoundException("User", id.toString())
            //actualizar los campos
            user.status = command.status
            user.name = command.name
            try {
                repository.update(user)
                UserDto.fromDomain(user)
            }catch (e: Exception){ //ConstraintViolationException) {
                // Si la DB dice que ya existe, lanzar nuestra excepción
                throw AlreadyExistsException("User", "${command.name} ")
            }
        }
}