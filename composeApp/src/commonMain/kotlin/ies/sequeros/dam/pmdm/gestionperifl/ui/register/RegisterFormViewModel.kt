package ies.sequeros.dam.pmdm.gestionperifl.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ies.sequeros.dam.pmdm.gestionperifl.application.usercase.RegisterRequest
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

    // Regex simple para email
    private val emailPattern = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-zA-Z]{2,}$")

    fun onNameChange(name: String) {
        _state.update { it.copy(name = name, nameError = null) }
    }

    fun onEmailChange(email: String) {
        _state.update { it.copy(email = email, emailError = null) }
    }

    fun onPasswordChange(password: String) {
        _state.update { it.copy(password = password, passwordError = null) }
    }

    // Renombrado a onSubmit para claridad
    fun onSubmit() {
        val currentState = _state.value

        // 1. Validaciones
        var hasError = false

        if (currentState.name.isBlank()) {
            _state.update { it.copy(nameError = "El nombre no puede estar vacío") }
            hasError = true
        }

        if (!emailPattern.matches(currentState.email)) {
            _state.update { it.copy(emailError = "Email inválido") }
            hasError = true
        }

        if (currentState.password.length < 4) {
            _state.update { it.copy(passwordError = "La contraseña debe tener al menos 4 caracteres") }
            hasError = true
        }

        if (hasError) return

        // 2. Llamada al Caso de Uso
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                // Creamos el Request DTO
                val request = RegisterRequest(
                    username = currentState.name,
                    email = currentState.email,
                    password = currentState.password
                )

                // Llamamos al UseCase y obtenemos un Result
                val result = registerUseCase(request)

                result.onSuccess { response ->
                    // Éxito: El usuario se guardó en BD y tenemos la respuesta
                    println("Usuario registrado con ID: ${response.id}")
                    _state.update { it.copy(isLoading = false, isRegisterSuccess = true) }
                }.onFailure { error ->
                    // Error: Falló la API
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Error: ${error.message}"
                        )
                    }
                }

            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Error inesperado: ${e.message}"
                    )
                }
            }
        }
    }
}