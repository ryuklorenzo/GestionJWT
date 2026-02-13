package ies.sequeros.dam.pmdm.gestionperifl.ui.editprofile
data class EditProfileFormState(
    val username: String = "",
    val email: String = "",

    val usernameError: String? = null,
    val emailError: String? = null,

    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
) {
    val isValid: Boolean
        get() = usernameError == null &&
                emailError == null &&
                username.isNotBlank() &&
                email.isNotBlank()
}
