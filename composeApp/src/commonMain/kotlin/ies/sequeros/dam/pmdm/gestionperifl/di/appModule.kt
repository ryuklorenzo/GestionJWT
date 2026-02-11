package ies.sequeros.dam.pmdm.gestionperifl.di

import ies.sequeros.dam.pmdm.gestionperifl.application.usercase.LoginUseCase
import ies.sequeros.dam.pmdm.gestionperifl.application.usercase.RegisterUseCase
import ies.sequeros.dam.pmdm.gestionperifl.domain.repository.UserRepository
import ies.sequeros.dam.pmdm.gestionperifl.infraestructure.ktor.createHttpClient
import ies.sequeros.dam.pmdm.gestionperifl.infraestructure.repository.UserRepositoryImpl
import ies.sequeros.dam.pmdm.gestionperifl.infraestructure.storage.SettingsTokenStorage
import ies.sequeros.dam.pmdm.gestionperifl.infraestructure.storage.TokenStorage
import ies.sequeros.dam.pmdm.gestionperifl.ui.appsettings.AppSettings
import ies.sequeros.dam.pmdm.gestionperifl.ui.appsettings.AppViewModel
import ies.sequeros.dam.pmdm.gestionperifl.ui.login.LoginFormViewModel
import ies.sequeros.dam.pmdm.gestionperifl.ui.register.RegisterFormViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModulo = module {

    // 1. Infraestructura
    single {
        // Usa localhost para Desktop
        createHttpClient("http://localhost:8080/api/public/refresh")
    }

    // FALTABA ESTO: Almacenamiento
    single<TokenStorage> { SettingsTokenStorage() }

    // FALTABA ESTO: Repositorio
    single<UserRepository> { UserRepositoryImpl(get(), get()) }

    // 2. Casos de Uso
    single { AppSettings() }
    single { LoginUseCase(get()) }
    single { RegisterUseCase(get()) } // Ahora recibe UserRepository, no HttpClient

    // 3. ViewModels
    viewModel { AppViewModel(get()) }
    viewModel { LoginFormViewModel(get()) }

    // CORREGIDO: Usamos RegisterFormViewModel (el que tiene la lógica), no la clase vacía
    viewModel { RegisterFormViewModel(get()) }
}