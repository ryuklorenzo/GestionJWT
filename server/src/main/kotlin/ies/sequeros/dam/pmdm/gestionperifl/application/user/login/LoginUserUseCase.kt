package ies.sequeros.dam.pmdm.gestionperifl.application.user.login

import ies.sequeros.dam.pmdm.gestionperifl.application.exceptions.InvalidCredentialsException
import ies.sequeros.dam.pmdm.gestionperifl.application.services.ITokenService
import ies.sequeros.dam.pmdm.gestionperifl.domain.repositories.IUserRepository
import ies.sequeros.dam.pmdm.gestionperifl.domain.services.IPasswordEncoder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoginUserUseCase(
    val repository: IUserRepository,
    val passwordEnconder: IPasswordEncoder,
    val tokenService: ITokenService

) {
    suspend operator fun invoke(command: LoginUserCommand): LoginDto =
        withContext(Dispatchers.IO) {
            //se obtiene
            val user = repository.findByEmail(command.email)?:throw InvalidCredentialsException("User")
            val oldPassword= repository.getPasswordHash(command.email)?:throw InvalidCredentialsException("User")
            if (!passwordEnconder.matches(command.password, oldPassword)) {
                throw InvalidCredentialsException("User")

            }
            val authToken= tokenService.generateAuthTokens(user)
            //se han pasado todos los filtros y condiciones
            LoginDto(authToken.accessToken,authToken.idToken,authToken.expiresIn,
                        "Bearer",authToken.refreshToken)
        }
}