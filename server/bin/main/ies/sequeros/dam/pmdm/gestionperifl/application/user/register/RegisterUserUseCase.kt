package ies.sequeros.dam.pmdm.gestionperifl.application.user.register

import ies.sequeros.dam.pmdm.gestionperifl.application.commons.UserDto
import ies.sequeros.dam.pmdm.gestionperifl.application.exceptions.AlreadyExistsException
import ies.sequeros.dam.pmdm.gestionperifl.domain.repositories.IUserRepository
import ies.sequeros.dam.pmdm.gestionperifl.domain.services.IPasswordEncoder
import ies.sequeros.dam.pmdm.gestionperifl.infraestructure.exceptions.DatabaseOperationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.hibernate.exception.ConstraintViolationException
import org.hibernate.exception.JDBCConnectionException
import java.util.*

class RegisterUserUseCase(
    val repository: IUserRepository,
    val passwordEnconder: IPasswordEncoder,

    ) {
    suspend operator fun invoke(command: RegisterUserCommand): UserDto =
        withContext(Dispatchers.IO) {
            //SE DELEGA LA COMPROBACIÓN EN QUE SALTE UNA EXCEPCIÓN
            //se crea el objeto del dominio a partir de comando
            val item = command.toDomain(UUID.randomUUID())
            val password = passwordEnconder.encode(command.password)
                //se almacena en la base de datos
                val newUser = repository.create(item, password = password)
                //se devuelve el dto
                UserDto.fromDomain(newUser)
        }
}