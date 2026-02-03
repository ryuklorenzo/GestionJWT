package ies.sequeros.dam.pmdm.gestionperifl.ktor_config

import ies.sequeros.dam.pmdm.gestionperifl.application.user.changepassword.ChangePasswordCommand
import ies.sequeros.dam.pmdm.gestionperifl.application.user.delete.DeleteCommand
import ies.sequeros.dam.pmdm.gestionperifl.application.user.login.LoginUserCommand
import ies.sequeros.dam.pmdm.gestionperifl.application.user.refresh.RefreshTokenUserCommand
import ies.sequeros.dam.pmdm.gestionperifl.application.user.register.RegisterUserCommand
import ies.sequeros.dam.pmdm.gestionperifl.application.user.update.UpdateUserCommand
import ies.sequeros.dam.pmdm.gestionperifl.domain.entities.UserStatus
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*


fun Application.configureValidator() {
    install(RequestValidation) {
        val PASSWORD_REGEX = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,32}$")
        val EMAIL_REGEX = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
        val JWT_REGEX = Regex("^[A-Za-z0-9-_=]+\\.[A-Za-z0-9-_=]+\\.[A-Za-z0-9-_.+/=]*$")

        validate<UpdateUserCommand> { request ->
            val validStatuses = UserStatus.entries.map { it.name.lowercase() }

            when {

                request.name.length !in 2..100 ->
                    ValidationResult.Invalid("El nombre debe tener entre 2 y 100 caracteres")

                // Validación de Status (Debe estar en el Enum)
               // !validStatuses.contains(request.status.lowercase()) ->
                //    ValidationResult.Invalid("Estado no válido. Valores permitidos: ${validStatuses.joinToString(", ")}")

                else -> ValidationResult.Valid
            }
        }
        validate<RegisterUserCommand> { request ->
            when {
                request.email.length !in 5..255 ->
                    ValidationResult.Invalid("El email debe tener entre 5 y 255 caracteres")
                !EMAIL_REGEX.matches(request.email) ->
                    ValidationResult.Invalid("El formato del email no es válido")
                request.username.length !in 8..64 ->
                    ValidationResult.Invalid("El nombre de usuario debe tener entre 8 y 64 caracteres")
                request.password.length !in 8..32 ->
                    ValidationResult.Invalid("La contraseña debe tener entre 8 y 32 caracteres")
                !PASSWORD_REGEX.matches(request.password) ->
                    ValidationResult.Invalid("La contraseña debe incluir mayúscula, minúscula, número y un carácter especial")

                else -> ValidationResult.Valid
            }
        }

        validate<RefreshTokenUserCommand> { request ->
            if (!JWT_REGEX.matches(request.refreshToken)) {
                ValidationResult.Invalid("El formato del refresh_token no es un JWT válido")
            } else {
                ValidationResult.Valid
            }
        }
        validate<LoginUserCommand> { request ->
            when {
                // Validación de Email: Longitud y Formato
                request.email.length !in 5..255 ->
                    ValidationResult.Invalid("El email debe tener entre 5 y 255 caracteres")

                !EMAIL_REGEX.matches(request.email) ->
                    ValidationResult.Invalid("El formato del email no es válido")

                // Validación de Password: minLength: 1
                request.password.isEmpty() ->
                    ValidationResult.Invalid("La contraseña no puede estar vacía")

                else -> ValidationResult.Valid
            }
        }
        validate<DeleteCommand> { request ->
            when {
                // Validación de longitud (minLength y maxLength del esquema)
                request.password.length !in 8..32 ->
                    ValidationResult.Invalid("La contraseña debe tener entre 8 y 32 caracteres")

                // Validación de patrón (el pattern del esquema)
                !PASSWORD_REGEX.matches(request.password) ->
                    ValidationResult.Invalid("La contraseña no cumple los requisitos: debe incluir mayúscula, minúscula, número y un carácter especial (@$!%*?&)")

                else -> ValidationResult.Valid
            }
        }
        validate<ChangePasswordCommand> { command ->
            when {
                // Validación de old_password (minLength: 1)
                command.oldPassword.isBlank() ->
                    ValidationResult.Invalid("La contraseña actual no puede estar vacía")

                // Validación de new_password (longitud y complejidad)
                command.newPassword.length !in 8..32 ->
                    ValidationResult.Invalid("La nueva contraseña debe tener entre 8 y 32 caracteres")

                !PASSWORD_REGEX.matches(command.newPassword) ->
                    ValidationResult.Invalid("La nueva contraseña debe contener mayúscula, minúscula, número y un carácter especial")

                else -> ValidationResult.Valid
            }
        }
    }
}