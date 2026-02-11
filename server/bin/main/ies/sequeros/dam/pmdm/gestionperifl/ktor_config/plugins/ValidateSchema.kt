package ies.sequeros.dam.pmdm.gestionperifl.ktor_config.plugins

import com.networknt.schema.InputFormat
    import com.networknt.schema.JsonSchemaFactory
import com.networknt.schema.SchemaLocation
import com.networknt.schema.SpecVersion
import com.networknt.schema.ValidationMessage
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.request.contentType
import io.ktor.server.request.httpMethod
import io.ktor.server.request.receiveText
import io.ktor.server.response.*
import io.ktor.server.response.respond
import kotlin.collections.isNotEmpty
import kotlin.collections.map
import kotlin.toString

// Configuración para el esquema específico
class SchemaConfig {
    var schemaPath: String = ""
}

val ValidateSchema = createRouteScopedPlugin(name = "ValidateSchema", createConfiguration = ::SchemaConfig) {

    val path = pluginConfig.schemaPath

    val factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V202012)
    val schema = factory.getSchema(SchemaLocation.of("resource:$path"))
    fun formatFriendlyMessage(error: ValidationMessage): String {
        return when (error.type) {
            "required" -> "Este campo es obligatorio."
            "minLength" -> "Es demasiado corto (mínimo ${error.arguments[0]} caracteres)."
            "maxLength" -> "Es demasiado largo (máximo ${error.arguments[0]} caracteres)."
            "format" -> if (error.arguments[0] == "email") "El formato de correo no es válido." else error.message
            "pattern" -> {
                // Personalizamos el mensaje según el campo que falló la regex
                when (error.property) {
                    "$.password", "$.new_password" -> "La contraseña debe tener entre 8-32 caracteres, incluir mayúsculas, minúsculas, un número y un carácter especial."
                    "$.refresh_token" -> "El token de refresco no tiene un formato válido."
                    else -> "El formato de este campo no es válido."
                }
            }
            "enum" -> "Valor no permitido. Opciones válidas: ${error.arguments[0]}"
            else -> error.message // Mensaje por defecto si no lo mapeamos
        }
    }
    onCall { call ->
        val method = call.request.httpMethod
        val contentType = call.request.contentType()

        // Validación de método y tipo de contenido
        if (method !in listOf(HttpMethod.Post, HttpMethod.Put, HttpMethod.Patch) ||
            !contentType.match(ContentType.Application.Json)
        ) {
            return@onCall
        }

        // habilitar DoubleReceive en la aplicación.
        val body = call.receiveText()

        try {
            val errors = schema.validate(body, InputFormat.JSON)

            if (errors.isNotEmpty()) {

                call.respond(
                    HttpStatusCode.BadRequest,
                    ErrorResponse(
                        error = "El JSON no cumple con el esquema",
                        detalles = errors.map {
                            val nombreCampo = if (it.type == "required") {
                                it.arguments[0].toString()
                            } else {
                                // Para otros errores, limpiamos el "$." de la ruta
                                it.instanceLocation.toString().replace("$.", "")
                            }
                            ValidationErrorDetail(
                                nombreCampo,
                                formatFriendlyMessage(it)
                            )
                        }
                    )
                )
                // terminar el pipeline aquí para que no llegue al handler de la ruta
                return@onCall
            }
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse(
                    error = "Json mal formado",
                    detalles = listOf(ValidationErrorDetail("syntax", e.message ?: "Invalid JSON"))
                )
            )
            return@onCall
        }
    }
}

