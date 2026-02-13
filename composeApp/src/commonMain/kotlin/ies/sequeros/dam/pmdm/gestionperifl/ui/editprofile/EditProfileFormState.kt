package ies.sequeros.dam.pmdm.gestionperifl.ui.editprofile

data class EditProfileFormState (
    val username: String = "",
    val email: String = "",
    val usernameError: String? = null,
    val emailError: String? = null,
    val isValid: Boolean = false,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)