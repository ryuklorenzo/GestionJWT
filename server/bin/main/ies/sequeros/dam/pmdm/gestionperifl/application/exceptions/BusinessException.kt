package ies.sequeros.dam.pmdm.gestionperifl.application.exceptions

class BusinessException(
    override val message: String
) : RuntimeException(message)