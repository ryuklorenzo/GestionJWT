package ies.sequeros.dam.pmdm.gestionperifl.di

import com.russhwolf.settings.Settings
import ies.sequeros.dam.pmdm.gestionperifl.application.session.SessionManager
import ies.sequeros.dam.pmdm.gestionperifl.application.usercase.*
import ies.sequeros.dam.pmdm.gestionperifl.domain.repository.UserRepository
import ies.sequeros.dam.pmdm.gestionperifl.infraestructure.repository.UserRepositoryImpl
import ies.sequeros.dam.pmdm.gestionperifl.infraestructure.storage.SettingsTokenStorage
import ies.sequeros.dam.pmdm.gestionperifl.infraestructure.storage.TokenStorage
import ies.sequeros.dam.pmdm.gestionperifl.ui.appsettings.AppViewModel
import ies.sequeros.dam.pmdm.gestionperifl.ui.deleteaccount.DeleteAccountViewModel
import ies.sequeros.dam.pmdm.gestionperifl.ui.editprofile.EditProfileViewModel
import ies.sequeros.dam.pmdm.gestionperifl.ui.login.LoginFormViewModel
import ies.sequeros.dam.pmdm.gestionperifl.ui.password.ChangePasswordViewModel
import ies.sequeros.dam.pmdm.gestionperifl.ui.principal.HomeFormVIewModel
import ies.sequeros.dam.pmdm.gestionperifl.ui.profile.ProfileViewModel
import ies.sequeros.dam.pmdm.gestionperifl.ui.register.RegisterFormViewModel
import io.ktor.client.HttpClient
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // 1. Settings (Preferencias)
    //single { Settings() }

    // 2. Storage: Decimos que cuando alguien pida TokenStorage, le damos SettingsTokenStorage
    single<TokenStorage> { SettingsTokenStorage(get()) }

    // 3. Session Manager (recibe TokenStorage)
    single { SessionManager(get()) }

    // 4. HTTP y API
    //single { HttpClient.client }
    //single { ApiService(get()) }

    // 5. Repositorios
    single<UserRepository> { UserRepositoryImpl(get(), get ()) }

    // 6. Casos de Uso
    single { LoginUseCase(get()) }
    single { RegisterUseCase(get()) }
    single { GetProfileUseCase(get()) }
    single { UpdateUserUseCase(get()) }
    single { ChangePasswordUseCase(get()) }
    single { DeleteUserUseCase(get()) }
    single { LogoutUseCase(get(), get()) } // Si existe

    // 7. ViewModels
    // IMPORTANTE: LoginFormViewModel ahora recibe 2 parámetros: LoginUseCase y SessionManager
    viewModel { LoginFormViewModel(get(), get()) }

    viewModel { RegisterFormViewModel(get()) }
    //viewModel { HomeFormVIewModel(get()) }
    viewModel { ProfileViewModel( ) }
    viewModel { EditProfileViewModel( ) }
    viewModel { ChangePasswordViewModel( ) }
    viewModel { DeleteAccountViewModel( ) }
    viewModel { AppViewModel(get()) }
}