package ies.sequeros.dam.pmdm.gestionperifl.ui.components.login

data class LoginState(
    // Campos del formulario
    val email: String = "ryuk@ryuk.com",
    val password: String = "12345678Aa*",

    // UI States
    val isLoading: Boolean = false,
    val isLoginSuccess: Boolean = false,
    val isValid:Boolean = false,
    // Errores específicos de campo (validación local)
    val emailError: String? = null,
    val passwordError: String? = null,

    // Error global (ej: "Credenciales incorrectas" o "No hay internet")
    val errorMessage: String? = null
)