package ies.sequeros.dam.pmdm.gestionperifl.ui.register

data class RegisterState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val nameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val isLoading: Boolean = false,
    val isRegisterSuccess: Boolean = false,
    val errorMessage: String? = null
) {
    val isValid: Boolean
        get() = name.isNotBlank() && email.isNotBlank() && password.isNotBlank() &&
                nameError == null && emailError == null && passwordError == null
}