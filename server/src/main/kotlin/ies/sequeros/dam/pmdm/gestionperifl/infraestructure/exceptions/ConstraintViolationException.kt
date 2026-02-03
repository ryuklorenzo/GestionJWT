package ies.sequeros.dam.pmdm.gestionperifl.infraestructure.exceptions

class ConstraintViolationException(val resourceName: String?, val reason: String?) : RuntimeException(
    String.format(
        "Conflicto en %s: %s",
        resourceName,
        reason
    )
)