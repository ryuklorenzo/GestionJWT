package ies.sequeros.dam.pmdm.gestionperifl.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ies.sequeros.dam.pmdm.gestionperifl.application.session.SessionManager
import ies.sequeros.dam.pmdm.gestionperifl.application.usercase.LoginUseCase
import ies.sequeros.dam.pmdm.gestionperifl.ui.components.login.LoginState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginFormViewModel(
    private val loginUseCase: LoginUseCase,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    private val isFormValid = MutableStateFlow(false)
    private val emailPattern = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-zA-Z]{2,}$")

    fun onEmailChange(email: String) {
        _state.update {
            it.copy(
                email = email,
                emailError = if (emailPattern.matches(email)) null else "Email no válido",
                errorMessage = null
            )
        }
        validateForm()
    }

    fun onPasswordChange(password: String) {
        _state.update {
            it.copy(
                password = password,
                passwordError = if (password.length >= 6) null else "Mínimo 6 caracteres",
                errorMessage = null
            )
        }
        validateForm()
    }

    private fun validateForm() {
        val s = _state.value
        val isValid = s.email.isNotBlank() &&
                s.password.isNotBlank() &&
                s.emailError == null &&
                s.passwordError == null

        isFormValid.value = isValid
        _state.update { it.copy(isValid = isValid) }
    }

    fun login() {
        //valirdar antes por si acaso
        if (!_state.value.isValid) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                val result = loginUseCase(_state.value)

                result.onSuccess { response ->
                    //guardamos tokens
                    sessionManager.saveSession(
                        accessToken = response.access_token,
                        idToken = response.id_token,
                        refreshToken = response.refresh_token
                    )

                    _state.update {
                        it.copy(
                            isLoading = false,
                            isLoginSuccess = true,
                            errorMessage = null
                        )
                    }
                }.onFailure { error ->
                    //fallos
                    val mensajeError = if (error.message?.contains("Credenciales") == true) {
                        "Credenciales incorrectas"
                    } else {
                        "Error al conectar: ${error.message}"
                    }

                    _state.update {
                        it.copy(
                            isLoading = false,
                            isLoginSuccess = false,
                            errorMessage = mensajeError
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
    fun clearFields() {
        _state.update {
            it.copy(
                email = "",
                password = "",
                errorMessage = null
            )
        }
    }
}