package ies.sequeros.dam.pmdm.gestionperifl.ui.editprofile


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ies.sequeros.dam.pmdm.gestionperifl.application.usercase.UpdateUserCommand
import ies.sequeros.dam.pmdm.gestionperifl.application.usercase.UpdateUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class EditProfileViewModel( private val updateUserUseCase: UpdateUserUseCase ) : ViewModel() {

    private val _state = MutableStateFlow(EditProfileFormState())
    val state: StateFlow<EditProfileFormState> = _state.asStateFlow()

    private val emailPattern = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-zA-Z]{2,}$")

    fun onUsernameChange(username: String) {
        _state.update {
            it.copy(
                username = username,
                usernameError = if (username.isNotBlank()) null else "Campo requerido",
                errorMessage = null
            )
        }
        validateForm()
    }

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

    private fun validateForm() {
        val s = _state.value
        val isValid = s.username.isNotBlank() &&
                s.email.isNotBlank() &&
                s.usernameError == null &&
                s.emailError == null
        _state.update { it.copy(isValid = isValid) }
    }

    fun updateProfile() {
        val s = _state.value
        if (!s.isValid) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }

            val result = updateUserUseCase(
                UpdateUserCommand(
                    name = s.username,
                    email = s.email
                    //status
                )
            )

            result.onSuccess {
                _state.update {
                    it.copy(
                        isLoading = false,
                        isSuccess = true
                    )
                }
            }.onFailure { error ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        isSuccess = false,
                        errorMessage = error.message ?: "Error desconocido",
                    )
                }
            }

        }

        fun resetState() {
            _state.value = EditProfileFormState()
        }

    }

}