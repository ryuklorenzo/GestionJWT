package ies.sequeros.dam.pmdm.gestionperifl.ui.editprofile


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ies.sequeros.dam.pmdm.gestionperifl.application.usercase.UpdateUserCommand
import ies.sequeros.dam.pmdm.gestionperifl.application.usercase.UpdateUserUseCase
import ies.sequeros.dam.pmdm.gestionperifl.domain.model.UserStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class EditProfileViewModel(private val updateUserUseCase: UpdateUserUseCase) : ViewModel() {

    private val _state = MutableStateFlow(EditProfileFormState())
    val state: StateFlow<EditProfileFormState> = _state.asStateFlow()

    fun onUsernameChange(username: String) {
        _state.update {
            it.copy(
                username = username,
                usernameError = if (username.isNotBlank()) null else "Campo requerido",
                errorMessage = null
            )
        }
    }

    fun onStatusChange(status: UserStatus) {
        _state.update {
            it.copy(
                status = status,
                statusError = null,
                errorMessage = null
            )
        }
    }


    fun updateProfile() {
        val s = _state.value
        if (!s.isValid) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }

            val result = updateUserUseCase(
                UpdateUserCommand(
                    name = s.username,
                    status = s.status!!
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