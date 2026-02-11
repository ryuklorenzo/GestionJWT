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
import ies.sequeros.dam.pmdm.gestionperifl.ui.register.RegisterFormViewModel // Ojo: Usa RegisterFormViewModel si es el que tiene la lógica
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModulo = module {

    // 1. Infraestructura: Cliente HTTP (Configurado para Desktop)
    single {
        createHttpClient(
            refreshUrl = "http://localhost:8080/api/public/refresh"
        )
    }

    // 2. Almacenamiento: TokenStorage (Faltaba esto)
    single<TokenStorage> { SettingsTokenStorage() }

    // 3. Repositorio: UserRepository (Faltaba esto)
    // Koin inyectará automáticamente el HttpClient y el TokenStorage definidos arriba
    single<UserRepository> { UserRepositoryImpl(get(), get()) }


    // 4. Capa de Aplicación (Casos de Uso)
    single { AppSettings() }
    single { LoginUseCase(get()) }
    single { RegisterUseCase(get()) }

    // 5. Capa de Presentación (ViewModels)
    viewModel { AppViewModel(get()) }
    viewModel { LoginFormViewModel(get()) }

    // Importante: Asegúrate de inyectar el ViewModel correcto.
    // En tus archivos anteriores creamos 'RegisterFormViewModel'.
    viewModel { RegisterFormViewModel(get()) }
}