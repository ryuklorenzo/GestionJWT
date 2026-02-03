package ies.sequeros.dam.pmdm.gestionperifl.ui.appsettings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AppViewModel(
    private val settings: AppSettings,

) : ViewModel() {
    val isDarkMode = settings.isDarkMode
    //se toma el valor de la sesion


    fun toggleTheme() = settings.toggleDarkMode()
    fun setDarkMode() {
        settings.setDarkMode()
    }
    fun setLighMode() {
        settings.setLightMode()
    }
    fun swithMode() {
        settings.toggleDarkMode()
    }

    init {
       /* userSessionManager.isLoggedIn.onEach {
                if (it==true) {
                    // El usuario acaba de entrar o la sesión se validó
                    checkSession()
                }
            }
            .launchIn(viewModelScope) // Se cancela automáticamente cuando el ViewModel muere
       // checkSession()*/
    }



}