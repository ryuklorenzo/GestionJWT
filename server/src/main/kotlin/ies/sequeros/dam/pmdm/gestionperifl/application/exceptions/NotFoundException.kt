package ies.sequeros.dam.pmdm.gestionperifl.application.exceptions

class NotFoundException(
    val resourceName: String,
    val identifier: String,
    override val message: String = "No se encontró '$resourceName' con identificador: $identifier"
) : RuntimeException(message)