package ies.sequeros.dam.pmdm.gestionperifl.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ies.sequeros.dam.pmdm.gestionperifl.application.usercase.RegisterUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterState())
    val state = _state.asStateFlow()

    fun onNameChange(name: String) {
        _state.update { it.copy(name = name, nameError = null) }
    }

    fun onEmailChange(email: String) {
        _state.update { it.copy(email = email, emailError = null) }
    }

    fun onPasswordChange(password: String) {
        _state.update { it.copy(password = password, passwordError = null) }
    }

    fun register() {
        val currentState = _state.value

        // Validaciones básicas
        if (currentState.name.isBlank()) {
            _state.update { it.copy(nameError = "El nombre no puede estar vacío") }
            return
        }
        if (currentState.email.isBlank() || !currentState.email.contains("@")) {
            _state.update { it.copy(emailError = "Email inválido") }
            return
        }
        if (currentState.password.length < 4) {
            _state.update { it.copy(passwordError = "La contraseña debe tener al menos 4 caracteres") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                // Asumimos que el UseCase lanza excepción si falla o devuelve un Result
                // Llamamos al caso de uso. El ID lo generará el backend (UUID) y la imagen/estado serán null/default.
                RegisterUseCase(
                    username = currentState.name,
                    email = currentState.email,
                    password = currentState.password
                )

                _state.update { it.copy(isLoading = false, isRegisterSuccess = true) }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Error al registrar: ${e.message ?: "Desconocido"}"
                    )
                }
            }
        }
    }
}