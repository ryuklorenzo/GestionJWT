package ies.sequeros.dam.pmdm.gestionperifl.ui.password

data class ChangePasswordFormState(
    val oldPassword: String = "",
    val newPassword: String = "",
    val oldPasswordError: String? = null,
    val newPasswordError: String? = null,
    val isValid: Boolean = false,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)
