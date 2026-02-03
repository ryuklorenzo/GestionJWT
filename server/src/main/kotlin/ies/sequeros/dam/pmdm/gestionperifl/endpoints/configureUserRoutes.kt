package ies.sequeros.dam.pmdm.gestionperifl.endpoints

import ies.sequeros.dam.pmdm.gestionperifl.endpoints.publico.loginEndPoint
import ies.sequeros.dam.pmdm.gestionperifl.endpoints.publico.refreshEndPoint
import ies.sequeros.dam.pmdm.gestionperifl.endpoints.publico.registerEndPoint
import ies.sequeros.dam.pmdm.gestionperifl.endpoints.user.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Route.configureUserRoutes() {

    route("api") {
        route("public") {
            registerEndPoint()
            loginEndPoint()
            refreshEndPoint()
        }
        //para el acceso al endpoint se ha de tener un token válido
        authenticate("auth-user") {
            route("users") {
                route("me") {
                    changePasswordEndPoint()
                    deleteUserEndPoint()
                    profileEndPoint()
                    updateUserEndPoint()
                    updateUserImageEndPoint()
                }
            }
        }
    }
}
