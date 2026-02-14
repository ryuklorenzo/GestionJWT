package ies.sequeros.dam.pmdm.gestionperifl.infraestructure.storage

import ies.sequeros.dam.pmdm.gestionperifl.domain.model.AuthTokens

interface TokenStorage {

    // Métodos individuales
    fun saveAccessToken(token: String)
    fun saveRefreshToken(token: String?)
    fun saveIdToken(token: String?)

    // Métodos para guardar/recuperar
    fun saveAllTokens(tokens: AuthTokens)

    // Métodos de recuperación
    fun getAccessToken(): String?
    fun getRefreshToken(): String?
    fun getIdToken(): String?

    // Limpieza
    fun clear()
}