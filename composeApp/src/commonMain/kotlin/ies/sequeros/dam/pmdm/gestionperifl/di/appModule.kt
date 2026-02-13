package ies.sequeros.dam.pmdm.gestionperifl.di

import ies.sequeros.dam.pmdm.gestionperifl.application.session.SessionManager
import ies.sequeros.dam.pmdm.gestionperifl.application.usercase.*
import ies.sequeros.dam.pmdm.gestionperifl.domain.repository.UserRepository
import ies.sequeros.dam.pmdm.gestionperifl.infraestructure.ktor.createHttpClient
import ies.sequeros.dam.pmdm.gestionperifl.infraestructure.repository.UserRepositoryImpl
import ies.sequeros.dam.pmdm.gestionperifl.infraestructure.storage.SettingsTokenStorage
import ies.sequeros.dam.pmdm.gestionperifl.infraestructure.storage.TokenStorage
import ies.sequeros.dam.pmdm.gestionperifl.ui.appsettings.AppSettings
import ies.sequeros.dam.pmdm.gestionperifl.ui.appsettings.AppViewModel
import ies.sequeros.dam.pmdm.gestionperifl.ui.login.LoginFormViewModel
import ies.sequeros.dam.pmdm.gestionperifl.ui.password.ChangePasswordViewModel
import ies.sequeros.dam.pmdm.gestionperifl.ui.register.RegisterFormViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModulo = module {

    single {
        createHttpClient("http://localhost:8080/api/public/refresh")
    }

    single { SettingsTokenStorage(get()) }
    single { SessionManager(get()) }
    single<TokenStorage> { get<SettingsTokenStorage>() }

    single<UserRepository> { UserRepositoryImpl(get(), get()) }

    single { UserRepositoryImpl(get(), get()) }

    single { AppSettings() }
    single { LoginUseCase(get()) }
    single { RegisterUseCase(get()) }
    single { ChangePasswordUseCase(get()) }
    single { GetProfileUseCase(get()) }
    single { LogoutUseCase(get(), get()) }
    single { UpdateUserUseCase(get()) }

    viewModel { AppViewModel(get()) }
    viewModel { LoginFormViewModel(get(), get()) }

    viewModel { RegisterFormViewModel(get()) }
    viewModel { ChangePasswordViewModel(get()) }

}