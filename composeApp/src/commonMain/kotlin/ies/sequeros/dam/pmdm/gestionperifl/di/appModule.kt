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

    // --- INFRAESTRUCTURA ---

    // 1. Almacenamiento local (TokenStorage)
    // Usamos SettingsTokenStorage que implementa la interfaz TokenStorage
    single<TokenStorage> { SettingsTokenStorage() }

    // 2. Cliente HTTP
    // IMPORTANTE:
    // - Si pruebas en Android Emulator usa: "http://10.0.2.2:8080/api/public/refresh"
    // - Si pruebas en Desktop/Web usa: "http://localhost:8080/api/public/refresh"
    single {
        createHttpClient(
            refreshUrl = "http://localhost:8080/api/public/refresh" // Ajusta según tu entorno
        )
    }

    // 3. Repositorios
    // Inyectamos HttpClient (get()) y TokenStorage (get())
    single<UserRepository> { UserRepositoryImpl(get(), get()) }


    // --- CAPA DE APLICACIÓN (Casos de Uso) ---

    single { LoginUseCase(get()) }
    single { RegisterUseCase(get()) }


    // --- CAPA DE PRESENTACIÓN (ViewModels y Estados globales) ---

    single { AppSettings() } // Configuración de la app (Tema, idioma, etc.)

    viewModel { AppViewModel(get()) }
    viewModel { LoginFormViewModel(get()) }

    // Aquí inyectamos el ViewModel del registro pasando el caso de uso
    viewModel { RegisterFormViewModel(get()) }
}