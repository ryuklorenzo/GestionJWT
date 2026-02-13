package ies.sequeros.dam.pmdm.gestionperifl.ktor_config

import ies.sequeros.dam.pmdm.gestionperifl.Greeting
import ies.sequeros.dam.pmdm.gestionperifl.endpoints.configureUserRoutes
import io.ktor.http.content.MultiPartData
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.doublereceive.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.utils.io.readRemaining
import kotlinx.io.readByteArray
import java.io.File

suspend fun processFile(multipart: MultiPartData): Pair<String, ByteArray> {
    var bytes: ByteArray? = null
    var nombreOriginal: String? = null
    multipart.forEachPart { part ->
        when (part) {
            is PartData.FileItem -> {
                nombreOriginal = part.originalFileName
                // Leer el contenido del archivo en un ByteArray
                nombreOriginal = part.originalFileName
                val channel = part.provider()
                bytes = channel
                    .readRemaining()
                    .readByteArray()
            }

            else -> {}
        }
        part.dispose()
    }
    return Pair(nombreOriginal, bytes) as Pair<String, ByteArray>
}


fun Application.configureRouting() {
    //install(Resources)
    //permite leer varias veces el body, por ejemplo
    //validar el json del body y que pase al siguiente estado en
    //el pipeline
    install(DoubleReceive)
    //rutas
    routing {
        //staticFiles("/resources", File("uploads"))
        staticFiles(
            remotePath = "/uploads",
            //en caso de no existir la crea
            dir = File("uploads").apply { if (!exists()) mkdirs() }
        )
        get("/") {
            call.respondText("Ktor: ${Greeting().greet()}")
        }

        //instanciar los endpoints del usuario
      configureUserRoutes()

    }
}
