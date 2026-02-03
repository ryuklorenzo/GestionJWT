package ies.sequeros.dam.pmdm.gestionperifl.ktor_config

import ies.sequeros.dam.pmdm.gestionperifl.application.services.ITokenService
import ies.sequeros.dam.pmdm.gestionperifl.di.appModulo
import ies.sequeros.dam.pmdm.gestionperifl.infraestructure.services.TokenService
import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureKoin() {
    install(Koin) {
        slf4jLogger()
        val config = environment.config
        val jwtSecret = config.property("jwt.secret").getString()
        val jwtIssuer = config.property("jwt.issuer").getString()
        val jwtAudience = config.property("jwt.audience").getString()

        //para poder inyectar la url en los endpoints en caso de ser necesario
        //ruta de las imágenes
        val storageBaseUrl = config.property("storage.baseUrl").getString()
        modules(module {
            single(named("baseUrl")) { storageBaseUrl }
            single<ITokenService> { TokenService(jwtSecret, jwtIssuer, storageBaseUrl) }
            includes(appModulo)
        })
    }
}