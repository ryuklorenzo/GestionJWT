package ies.sequeros.dam.pmdm.gestionperifl.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ies.sequeros.dam.pmdm.gestionperifl.application.usercase.GetProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel( private val getProfileUseCase: GetProfileUseCase): ViewModel() {

    private val _state = MutableStateFlow(ProfileFormState())
    val state: StateFlow<ProfileFormState> = _state.asStateFlow()

    init {
        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }

            val result = getProfileUseCase()

            result.onSuccess { profile ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        name = profile.name,
                        email = profile.email,
                        image = profile.image,
                        status = profile.status
                    )
                }
            }.onFailure { error ->
                _state.update { it.copy(isLoading = false, errorMessage = error.message ?: "Error desconocido") }
            }

        }
    }

}