package ies.sequeros.dam.pmdm.gestionperifl.application.user.refresh

import ies.sequeros.dam.pmdm.gestionperifl.application.exceptions.NotFoundException
import ies.sequeros.dam.pmdm.gestionperifl.application.services.ITokenService
import ies.sequeros.dam.pmdm.gestionperifl.domain.repositories.IUserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RefreshTokenUseCase(
    val repository: IUserRepository,
    val tokenService: ITokenService

) {
    suspend operator fun invoke(command: RefreshTokenUserCommand): RefreshDto =
        withContext(Dispatchers.IO) {
            val id=tokenService.validateToken(command.refreshToken)
            val user=repository.findById(id)?: throw NotFoundException("User","User not found")
            val authToken= tokenService.generateAuthTokens(user)
            RefreshDto(authToken.accessToken,authToken.idToken,authToken.expiresIn,
                "Bearer",authToken.refreshToken)

        }
}