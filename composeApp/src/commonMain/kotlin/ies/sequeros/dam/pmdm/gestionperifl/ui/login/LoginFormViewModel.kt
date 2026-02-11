package ies.sequeros.dam.pmdm.gestionperifl.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ies.sequeros.dam.pmdm.gestionperifl.application.usercase.LoginUseCase

import ies.sequeros.dam.pmdm.gestionperifl.ui.components.login.LoginState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginFormViewModel(
    //inyectar caso de uso
   // val loginUseCase: LoginUseCase
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()
    val isFormValid = MutableStateFlow(false)
    val emailPattern = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-zA-Z]{2,}$")

    fun onEmailChange(email: String) {
        _state.update {
            it.copy(
                email = email,
                emailError = if (emailPattern.matches(email))  null else "Email no válido"
            )
        }
        validateForm()
    }

    fun onPasswordChange(password: String) {
        _state.update {
            it.copy(
                password = password,
                passwordError = if (password.length >= 6) null else "Mínimo 6 caracteres"
            )
        }
        validateForm()
    }

    private fun validateForm() {
        val s = _state.value
        isFormValid.value = s.email.isNotBlank() &&
                s.password.isNotBlank() &&
                s.emailError == null &&
                s.passwordError == null
        _state.value=state.value.copy( isValid = isFormValid.value)
    }

    fun login() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                //cargando
                _state.value = state.value.copy(isLoading = true)
                //crear el comando, llamar al caso de uso
                //que devuelve ok, o un error en el result

                val loginState =
                    ies.sequeros.dam.pmdm.gestionperifl.ui.components.login.LoginState(
                        email = state.value.email,
                        password = state.value.password
                    )
                val result= loginUseCase(loginState).onSuccess{
                    //_state.value = _state.value.copy(isLoginSuccess = true)
                    _state.update { it.copy(isLoading = false, isLoginSuccess = true) }
                }.onFailure {
                    _state.update { it.copy(isLoading = false, isLoginSuccess = false) }
                    //meter aqui el error

                }

            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Error al conectar: ${e.message}"
                    )
                }
            } finally {
                _state.value = _state.value.copy(isLoading = false)
            }
        }
    }
}