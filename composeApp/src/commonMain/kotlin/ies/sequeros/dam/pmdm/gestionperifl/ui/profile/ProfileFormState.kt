package ies.sequeros.dam.pmdm.gestionperifl.ui.profile

data class ProfileFormState(
    val isLoading: Boolean = false,
    val name: String = "",
    val email: String = "",
    val image: String = "",
    val status: String = "",
    val errorMessage: String? = null
)
