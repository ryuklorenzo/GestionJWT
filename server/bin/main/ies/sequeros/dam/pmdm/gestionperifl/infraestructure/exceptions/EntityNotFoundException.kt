package ies.sequeros.dam.pmdm.gestionperifl.infraestructure.exceptions


class EntityNotFoundException(
    val resourceName: String?,
    val operation: String?, cause: Throwable
) : RuntimeException(
    String.format(
        "Error al %s en %s: %s",
        operation,
        resourceName, cause.message
    ), cause
)