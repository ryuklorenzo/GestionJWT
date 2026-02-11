package ies.sequeros.dam.pmdm.gestionperifl

import ies.sequeros.dam.pmdm.gestionperifl.ktor_config.configureCors
import ies.sequeros.dam.pmdm.gestionperifl.ktor_config.configureKoin
import ies.sequeros.dam.pmdm.gestionperifl.ktor_config.configureRouting
import ies.sequeros.dam.pmdm.gestionperifl.ktor_config.configureSecurity
import ies.sequeros.dam.pmdm.gestionperifl.ktor_config.configureSerialization
import ies.sequeros.dam.pmdm.gestionperifl.ktor_config.configureStatusPages
import ies.sequeros.dam.pmdm.gestionperifl.ktor_config.configureValidator
import io.ktor.server.application.*
import io.ktor.server.netty.EngineMain
import java.util.UUID


fun main(args: Array<String>): Unit = EngineMain.main(args)
fun Application.module() {
    //el orden importa
    //control de excepciones y http code status
    configureCors()
    configureStatusPages()

    configureValidator()
    //Koin
    configureKoin()
    //configuración de json
    configureSerialization()
    //JWT
    configureSecurity()
    //routing
    configureRouting()
}
