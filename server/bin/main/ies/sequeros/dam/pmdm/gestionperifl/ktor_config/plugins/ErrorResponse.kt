package ies.sequeros.dam.pmdm.gestionperifl.ktor_config.plugins

import kotlinx.serialization.Serializable

// DTOs para la respuesta de error
@Serializable
data class ValidationErrorDetail(val field: String, val mensaje: String)

@Serializable
data class ErrorResponse(val error: String, val detalles: List<ValidationErrorDetail>?= emptyList())
