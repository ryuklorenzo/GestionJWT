package ies.sequeros.dam.pmdm.gestionperifl.application.exceptions

class AlreadyExistsException(
    val resourceName: String,
    val fieldValue: String,
    override val message: String = "Duplicados en la tabla '$resourceName' , operacion: '$fieldValue'."
) : RuntimeException(message)