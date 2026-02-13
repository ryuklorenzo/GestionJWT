package ies.sequeros.dam.pmdm.gestionperifl.di

import ies.sequeros.dam.pmdm.gestionperifl.application.user.changepassword.ChangePasswordUseCase
import ies.sequeros.dam.pmdm.gestionperifl.application.user.delete.DeleteUseCase
import ies.sequeros.dam.pmdm.gestionperifl.application.user.getmeprofile.GetMyProfileUseCase
import ies.sequeros.dam.pmdm.gestionperifl.application.user.login.LoginUserUseCase
import ies.sequeros.dam.pmdm.gestionperifl.application.user.refresh.RefreshTokenUseCase
import ies.sequeros.dam.pmdm.gestionperifl.application.user.register.RegisterUserUseCase
import ies.sequeros.dam.pmdm.gestionperifl.application.user.update.UpdateUserCasoDeUso
import ies.sequeros.dam.pmdm.gestionperifl.application.user.updateimage.UpdateImageUserCasoDeUso
import ies.sequeros.dam.pmdm.gestionperifl.domain.repositories.IFilesRepository
import ies.sequeros.dam.pmdm.gestionperifl.domain.repositories.IUserRepository
import ies.sequeros.dam.pmdm.gestionperifl.domain.services.IPasswordEncoder
import ies.sequeros.dam.pmdm.gestionperifl.infraestructure.exceptions.DatabaseOperationException
import ies.sequeros.dam.pmdm.gestionperifl.infraestructure.repositories.FilesRepository
import ies.sequeros.dam.pmdm.gestionperifl.infraestructure.repositories.UserJPARepository
import ies.sequeros.dam.pmdm.gestionperifl.infraestructure.services.BCryptEncoderAdapter
import jakarta.persistence.EntityManagerFactory
import jakarta.persistence.Persistence
import org.koin.dsl.module
import org.koin.dsl.onClose

val appModulo = module {
    single<EntityManagerFactory> {
        try {
            Persistence.createEntityManagerFactory("gestionJWTUnidadPersistencia")
        } catch (e: Exception) {
            println("ERROR FATAL PERSISTENCIA: ${e.message}")
            throw DatabaseOperationException("Koin", "Error de conexión: DB inaccesible", e)
        }
    }.onClose {
        //se cierra la factoria
        it?.close()
    }
    // Repositorios
    single<IUserRepository> {
        try {
            UserJPARepository(get())
        } catch (e: Exception) {
            println("ERROR FATAL PERSISTENCIA: ${e.message}")
            throw e
        }
    }
    //repositorio ficheros
    single<IFilesRepository> { FilesRepository() }

    //codificador
    single<IPasswordEncoder> { BCryptEncoderAdapter() }
    factory { RegisterUserUseCase(get(), get()) }
    factory { ChangePasswordUseCase(get(), get()) }
    factory { DeleteUseCase(get(), get(), get()) }
    factory { GetMyProfileUseCase(get(), get()) }
    factory { LoginUserUseCase(get(), get(), get()) }
    factory { RefreshTokenUseCase(get(), get()) }
    factory { UpdateUserCasoDeUso(get()) }
    factory { UpdateImageUserCasoDeUso(get(), get()) }

}