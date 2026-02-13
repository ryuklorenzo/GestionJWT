package ies.sequeros.dam.pmdm.gestionperifl.ui.imagen

import io.ktor.http.cio.*

data class ChangeImageFormState (
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)