package ies.sequeros.dam.pmdm.gestionperifl.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ies.sequeros.dam.pmdm.gestionperifl.application.usercase.RegisterUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterFormViewModel(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterState())
    val state = _state.asStateFlow()

    // Regex mejorada
    private val emailPattern = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-zA-Z]{2,}$")
    private val passwordPattern = Regex("""^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[\W_]).{8,}$""")

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
        var hasError = false

        // Validaciones
        if (currentState.name.isBlank() || currentState.name.length < 7) {
            _state.update { it.copy(nameError = "El nombre debe tener al menos 8 letras") }
            hasError = true
        }
        if (!emailPattern.matches(currentState.email)) {
            _state.update { it.copy(emailError = "Email inválido") }
            hasError = true
        }
        if (!passwordPattern.matches(currentState.password)) {
            _state.update { it.copy(passwordError = "Mínimo 4 caracteres") }
            hasError = true
        }

        if (hasError) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                val success = registerUseCase(
                    username = currentState.name,
                    email = currentState.email,
                    password = currentState.password
                )

                if (success) {
                    _state.update { it.copy(isLoading = false, isRegisterSuccess = true) }
                } else {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Error al registrar. El usuario quizás ya existe."
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Error de conexión: ${e.message ?: "Desconocido"}"
                    )
                }
            }
        }
    }
}