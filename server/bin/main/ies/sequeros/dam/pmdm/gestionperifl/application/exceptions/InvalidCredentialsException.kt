package ies.sequeros.dam.pmdm.gestionperifl.application.exceptions

class InvalidCredentialsException(
    val reason: String,
    override val message: String = "Credenciales: $reason"
) : RuntimeException(message)