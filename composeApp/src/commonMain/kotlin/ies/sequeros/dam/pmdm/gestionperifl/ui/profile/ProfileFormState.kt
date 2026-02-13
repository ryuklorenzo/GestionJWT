package ies.sequeros.dam.pmdm.gestionperifl.ui.profile

data class ProfileFormState(
    val isLoading: Boolean = false,
    val username: String = "",
    val email: String = "",
    val createdAt: String = "",
    val errorMessage: String? = null
)
