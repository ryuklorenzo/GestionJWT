package ies.sequeros.dam.pmdm.gestionperifl.ktor_config

import ies.sequeros.dam.pmdm.gestionperifl.application.exceptions.AlreadyExistsException
import ies.sequeros.dam.pmdm.gestionperifl.application.exceptions.BusinessException
import ies.sequeros.dam.pmdm.gestionperifl.application.exceptions.InvalidCredentialsException
import ies.sequeros.dam.pmdm.gestionperifl.application.exceptions.NotFoundException
import ies.sequeros.dam.pmdm.gestionperifl.infraestructure.exceptions.ConstraintViolationException
import ies.sequeros.dam.pmdm.gestionperifl.infraestructure.exceptions.DatabaseOperationException
import ies.sequeros.dam.pmdm.gestionperifl.infraestructure.exceptions.EntityNotFoundException
import ies.sequeros.dam.pmdm.gestionperifl.ktor_config.plugins.ErrorResponse
import ies.sequeros.dam.pmdm.gestionperifl.ktor_config.plugins.ValidationErrorDetail
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.JsonConvertException
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.application.log
import io.ktor.server.plugins.requestvalidation.RequestValidationException


import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import kotlinx.serialization.MissingFieldException
import kotlinx.serialization.SerializationException
import org.hibernate.exception.JDBCConnectionException
import org.postgresql.util.PSQLException
import java.net.ConnectException
import javax.naming.AuthenticationException

fun Application.configureStatusPages() {
    //configuración
    install(StatusPages) {
        /**
         * EXCEPCIONES CAPA DE PRESENTACIÓN/ENDPOINTS
         */
        // Captura específicamente errores de campos faltantes
        exception<RequestValidationException> { call, cause ->
            call.respond(
                HttpStatusCode.BadRequest,
                mapOf(
                    "error" to "Datos de entrada inválidos",
                    "motivos" to cause.reasons // Lista de mensajes de error definidos arriba
                )
            )
        }
        exception<MissingFieldException> { call, cause ->
            val missingFields = cause.missingFields.joinToString(", ")
            call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse(
                    error = "Datos incompletos",
                    detalles = listOf(
                        ValidationErrorDetail(
                            missingFields,
                             "Este campo es requerido por el servidor."
                        )
                    )
                )
            )
        }
        exception<JsonConvertException> { call, cause ->
            val internalCause = cause.cause
            val (mensajeAmigable, campoError) = when (internalCause) {
                is MissingFieldException -> {
                    "Faltan campos obligatorios en el JSON." to internalCause.missingFields.joinToString(", ")
                }
                is SerializationException -> {
                    if (internalCause.message?.contains("Encountered unknown key") == true) {
                        "El JSON contiene campos no permitidos por el servidor." to "unknown_field"
                    } else {
                        "Error de formato o tipo de dato incorrecto." to "payload"
                    }
                }
                else -> {
                    "El formato del JSON es inválido." to "generic"
                }
            }

            call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse(
                    error = "Error de serialización",
                    detalles = listOf(
                        ValidationErrorDetail(campoError, mensajeAmigable)
                    )
                )
            )
        }
        exception<RequestValidationException> { call, cause ->
            // Devolvemos un 400 Bad Request con los motivos del fallo
            call.respond(
                HttpStatusCode.BadRequest,
                mapOf("errors" to cause.reasons)
            )
        }
        // Captura errores de formato (ej: enviar un número donde va un string)
        exception<SerializationException> { call, cause ->
            call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse(
                    error = "Error de formato en el JSON",
                    detalles = listOf(
                        ValidationErrorDetail(
                             "payload",
                             cause.message ?: "El formato del JSON no coincide con el modelo esperado."
                        )
                    )
                )
            )
        }
        /**
         * EXCEPCIONES CAPA DE APLICACIÓN
         */
        exception<AlreadyExistsException>{ call, cause ->
            val error = ErrorResponse(
                error = "AlreadyExistsException",
                detalles = listOf(
                    ValidationErrorDetail(
                        "text",
                        cause.message.toString()
                    )
                )
            )
            call.respond(HttpStatusCode.Conflict,error)

        }
        exception<BusinessException>{ call, cause ->
            val error = ErrorResponse(
                error = "BusinessException",
                detalles = listOf(
                    ValidationErrorDetail(
                        "text",
                        cause.message.toString()
                    )
                )
            )
            call.respond(HttpStatusCode.Conflict,error)
        }
        exception<InvalidCredentialsException> { call, cause ->
            call.respondText(
                text = cause.message ?: "Credenciales incorrectas",
                status = HttpStatusCode.Unauthorized
            )
        }
        status(HttpStatusCode.Unauthorized) { call, status ->
            // Si el body ya tiene texto (puesto por la excepción de arriba), no lo sobrescribimos
            // Si está vacío, es que Ktor (JWT) lo rechazó directamente -> Token malo
            call.respondText(
                text = "Sesión expirada o token inválido",
                status = status
            )
        }
        exception<NotFoundException>{ call, cause ->
            val error = ErrorResponse(
                error = "NotFoundException",
                detalles = listOf(
                    ValidationErrorDetail(
                        "text",
                        cause.message.toString()
                    )
                )
            )
            call.respond(HttpStatusCode.NotFound,error)
        }

        /**
         * EXCEPCIONES CAPA DE INFRAESTRUCTURA
         */
        exception<ConstraintViolationException>{ call, cause ->
            val error = ErrorResponse(
                error = "AlreadyExistsException",
                detalles = listOf(
                    ValidationErrorDetail(
                        "text",
                        cause.message.toString()
                    )
                )
            )
            call.respond(HttpStatusCode.NotFound,error)
        }
        exception< DatabaseOperationException>{ call, cause ->
            val error = ErrorResponse(
                error = "DatabaseOperationException",
                detalles = listOf(
                    ValidationErrorDetail(
                        "text",
                        cause.message.toString()
                    )
                )
            )
            call.respond(HttpStatusCode.ServiceUnavailable,error)
        }
        exception<EntityNotFoundException>{ call, cause ->
            val error = ErrorResponse(
                error = "EntityNotFoundException",
                detalles = listOf(
                    ValidationErrorDetail(
                        "text",
                        cause.message.toString()
                    )
                )
            )
            call.respond(HttpStatusCode.NotFound,error)
        }

        exception<ConnectException>{ call, cause ->
            val error = ErrorResponse(
                error = "ConnectException",
                detalles = listOf(
                    ValidationErrorDetail(
                        "text",
                        cause.message.toString()
                    )
                )
            )
            call.respond(HttpStatusCode.NotFound,error)
        }
        exception<Throwable> { call, ex ->

            //call.application.log.error("Error no controlado", ex)
            //obtener la causa general
            val rootCause = ex.getRootCause()
            when (rootCause) {
                is java.net.ConnectException ->{
                    call.respond(HttpStatusCode.ServiceUnavailable,rootCause.message.toString())
                }
                is org.postgresql.util.PSQLException ->{
                    call.respond(HttpStatusCode.ServiceUnavailable,rootCause.message.toString())
                }
                else -> call.respond(HttpStatusCode.InternalServerError,rootCause.message.toString())
            }

        }
    }
}
//para obtener la causa principal
fun Throwable.getRootCause(): Throwable {

    var rootCause = this
    while (rootCause.cause != null && rootCause.cause != rootCause) {
        rootCause = rootCause.cause!!
    }
    return rootCause
}