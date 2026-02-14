package ies.sequeros.dam.pmdm.gestionperifl.ui.components.login

data class LoginState(
    // Campos del formulario
    val email: String = "",
    val password: String = "",

    // UI States
    val isLoading: Boolean = false,
    val isLoginSuccess: Boolean = false,
    val isValid:Boolean = false,
    // Errores específicos de campo
    val emailError: String? = null,
    val passwordError: String? = null,

    // Error global
    val errorMessage: String? = null
)