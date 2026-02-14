package ies.sequeros.dam.pmdm.gestionperifl.ui.editprofile

import ies.sequeros.dam.pmdm.gestionperifl.domain.model.UserStatus

data class EditProfileFormState(
    val username: String = "",
    val status: UserStatus? = null,

    val usernameError: String? = null,
    val statusError: String? = null,

    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
) {
    val isValid: Boolean
        get() = usernameError == null &&
                username.isNotBlank() &&
                status != null
}
