package ies.sequeros.dam.pmdm.gestionperifl.application.user.delete

import ies.sequeros.dam.pmdm.gestionperifl.application.exceptions.BusinessException
import ies.sequeros.dam.pmdm.gestionperifl.application.exceptions.InvalidCredentialsException
import ies.sequeros.dam.pmdm.gestionperifl.application.exceptions.NotFoundException
import ies.sequeros.dam.pmdm.gestionperifl.application.services.ITokenService
import ies.sequeros.dam.pmdm.gestionperifl.application.user.changepassword.ChangePasswordCommand
import ies.sequeros.dam.pmdm.gestionperifl.application.user.refresh.RefreshDto
import ies.sequeros.dam.pmdm.gestionperifl.application.user.refresh.RefreshTokenUserCommand
import ies.sequeros.dam.pmdm.gestionperifl.domain.repositories.IFilesRepository
import ies.sequeros.dam.pmdm.gestionperifl.domain.repositories.IUserRepository
import ies.sequeros.dam.pmdm.gestionperifl.domain.repositories.StorageEntity
import ies.sequeros.dam.pmdm.gestionperifl.domain.services.IPasswordEncoder
import ies.sequeros.dam.pmdm.gestionperifl.infraestructure.repositories.FilesRepository
import io.ktor.server.auth.AuthenticationFailedCause
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

class DeleteUseCase(
    val repository: IUserRepository,
    val passwordEnconder: IPasswordEncoder,
    val filesRepository: IFilesRepository

) {
    suspend operator fun invoke(id: UUID, command: DeleteCommand): Unit =
        withContext(Dispatchers.IO) {
            //val user=repository.findById(id)?: throw  InvalidCredentialsException("User")
            val oldPassword = repository.getPasswordHash(id) ?: throw InvalidCredentialsException("User")
            //no concide la contrasenya
            if (!passwordEnconder.matches(command.password, oldPassword)) {
                throw InvalidCredentialsException("User")
            }
            //se carga el usuario con ese id
            //primera opción da pistas
            //val user = repository.findById(id) ?: throw NotFoundException("User",id.to())
            val user = repository.findById(id) ?: throw InvalidCredentialsException("User",id.toString())

            //guardar la imagen para borrarla

            val image = user.image
            repository.delete(user)
            image?.let {
                filesRepository.delete(StorageEntity.USUARIO, it)
            }
        }
}
