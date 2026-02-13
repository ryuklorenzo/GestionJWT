package ies.sequeros.dam.pmdm.gestionperifl.ui.deleteaccount

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ies.sequeros.dam.pmdm.gestionperifl.application.usercase.DeleteUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DeleteAccountViewModel(private val deleteUserUseCase: DeleteUserUseCase): ViewModel() {

    private val _state = MutableStateFlow(DeleteAccountFormState())
    val state: StateFlow<DeleteAccountFormState> = _state.asStateFlow()

    fun deleteAccount(password: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }

            // Pasamos la contraseña
            val result = deleteUserUseCase(password)

            result.onSuccess {
                _state.update { it.copy(isLoading = false, isSuccess = true) }
            }.onFailure { error ->
                _state.update {
                    it.copy(isLoading = false, isSuccess = false, errorMessage = error.message ?: "Error desconocido")
                }
            }
        }
    }
    fun resetState() {
        _state.value = DeleteAccountFormState()
    }

}