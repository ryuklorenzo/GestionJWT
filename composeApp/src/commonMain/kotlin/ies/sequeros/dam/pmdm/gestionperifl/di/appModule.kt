package ies.sequeros.dam.pmdm.gestionperifl.di

import ies.sequeros.dam.pmdm.gestionperifl.application.usercase.LoginUseCase
import ies.sequeros.dam.pmdm.gestionperifl.infraestructure.ktor.createHttpClient
import ies.sequeros.dam.pmdm.gestionperifl.ui.appsettings.AppSettings
import ies.sequeros.dam.pmdm.gestionperifl.ui.appsettings.AppViewModel
import ies.sequeros.dam.pmdm.gestionperifl.ui.login.LoginFormViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module



val appModulo = module {

    /**
     * infraestructura
     */
    single {
        createHttpClient( //get(),
            "http://localhost:8080/api/public/refresh"
        )
    }
    //almacenamiento del token
    //repositorios
    /**
    capa de aplicación
    el sesion manager,
    el origen de los datos, se encarga de transforar el tokenstorage para trabajar con user
    casos de uso
     **/

    /**
    capa de presentación
     **/
    single { AppSettings() }
    single { LoginUseCase(get()) }
    viewModel { AppViewModel(get()) }
    viewModel { LoginFormViewModel(get()) }

}