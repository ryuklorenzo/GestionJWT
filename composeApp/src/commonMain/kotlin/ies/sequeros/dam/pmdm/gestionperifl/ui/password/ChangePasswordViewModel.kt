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

    fun onOldPasswordChange(old: String) {
        _state.update {
            it.copy(
                oldPassword = old,
                oldPasswordError = if (old.isNotBlank()) null else "Campo requerido",
                errorMessage = null
            )
        }
        validateForm()
    }

    fun onNewPasswordChange(new: String) {
        _state.update {
            it.copy(
                newPassword = new,
                newPasswordError = if (new.length >= 6) null else "Mínimo 6 caracteres",
                errorMessage = null
            )
        }
        validateForm()
    }

    private fun validateForm() {
        val s = _state.value
        val isValid = s.oldPassword.isNotBlank() &&
                s.newPassword.isNotBlank() &&
                s.oldPasswordError == null &&
                s.newPasswordError == null
        _state.update { it.copy(isValid = isValid) }
    }

    fun changePassword() {
        val s = _state.value
        if (!s.isValid) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }

            val result = changePasswordUseCase(s.oldPassword, s.newPassword)

            result.onSuccess {
                _state.value = _state.value.copy(isLoading = false, isSuccess = true)

            }.onFailure { error ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        isSuccess = false,
                        errorMessage = error.message ?: "Error desconocido"
                    )
                }
            }
        }
    }

    fun resetState() {
        _state.value = ChangePasswordFormState()
    }
}
