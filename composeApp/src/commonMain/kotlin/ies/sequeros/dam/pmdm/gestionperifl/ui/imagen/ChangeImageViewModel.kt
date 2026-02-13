package ies.sequeros.dam.pmdm.gestionperifl.ui.imagen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ies.sequeros.dam.pmdm.gestionperifl.application.usercase.ChangeUserImageUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ChangeImageViewModel(private val changeUserImageUseCase: ChangeUserImageUseCase) : ViewModel() {

    private val _state = MutableStateFlow(ChangeImageFormState())
    val state: StateFlow<ChangeImageFormState> = _state.asStateFlow()

    fun changeImage(userId: String, imageUrl: String) {

        viewModelScope.launch {

            _state.update {
                it.copy(isLoading = true, errorMessage = null)
            }

            val result = changeUserImageUseCase.invoke(userId, imageUrl)

            result.onSuccess {
                _state.update { it.copy(isLoading = false, isSuccess = false) }
            }

        }

    }

    fun resetState() {
        _state.value = ChangeImageFormState()
    }

}