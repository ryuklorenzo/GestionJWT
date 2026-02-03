package ies.sequeros.dam.pmdm.gestionperifl.application.user.getmeprofile

import ies.sequeros.dam.pmdm.gestionperifl.application.exceptions.InvalidCredentialsException
import ies.sequeros.dam.pmdm.gestionperifl.application.exceptions.NotFoundException
import ies.sequeros.dam.pmdm.gestionperifl.application.services.ITokenService
import ies.sequeros.dam.pmdm.gestionperifl.application.user.login.LoginDto
import ies.sequeros.dam.pmdm.gestionperifl.application.user.login.LoginUserCommand
import ies.sequeros.dam.pmdm.gestionperifl.domain.repositories.IFilesRepository
import ies.sequeros.dam.pmdm.gestionperifl.domain.repositories.IUserRepository
import ies.sequeros.dam.pmdm.gestionperifl.domain.services.IPasswordEncoder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

class GetMyProfileUseCase(
    val repository: IUserRepository,
    val fileRepository: IFilesRepository

) {
    suspend operator fun invoke(id: UUID): ProfileDto =
        withContext(Dispatchers.IO) {
            //se obtiene
            val user = repository.findById(id)?:throw NotFoundException("User","User not found")
            val imagen=user.image?:"default-avatar.png"
            //se han pasado todos los filtros y condiciones
            ProfileDto(
                user.id,user.name,user.email, imagen,user.status
            )
        }
}