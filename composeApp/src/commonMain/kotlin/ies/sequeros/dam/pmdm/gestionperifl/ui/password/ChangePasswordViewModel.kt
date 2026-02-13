package ies.sequeros.dam.pmdm.gestionperifl.ui.password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ies.sequeros.dam.pmdm.gestionperifl.application.usercase.ChangePasswordUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChangePasswordViewModel(
    private val changePasswordUseCase: ChangePasswordUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ChangePasswordFormState())
    val state: StateFlow<ChangePasswordFormState> = _state.asStateFlow()

    fun onOldPasswordChange(text: String) {
        _state.update { it.copy(oldPassword = text, errorMessage = null) }
    }

    fun onNewPasswordChange(text: String) {
        _state.update { it.copy(newPassword = text, errorMessage = null) }
    }

    fun submit() {
        val oldPass = _state.value.oldPassword
        val newPass = _state.value.newPassword

        if (oldPass.isBlank() || newPass.isBlank()) {
            _state.update { it.copy(errorMessage = "Rellena todos los campos") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            // Llamamos al caso de uso.
            // IMPORTANTE: oldPass viaja tal cual la escribió el usuario.
            val result = changePasswordUseCase(oldPass, newPass)

            result.onSuccess {
                _state.update { it.copy(isSuccess = true, isLoading = false) }
            }.onFailure { error ->
                // error.message contendrá "Contraseña incorrecta" o "Sesión expirada"
                // tal cual lo envió el servidor
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "Error desconocido"
                    )
                }
            }
        }
    }
}