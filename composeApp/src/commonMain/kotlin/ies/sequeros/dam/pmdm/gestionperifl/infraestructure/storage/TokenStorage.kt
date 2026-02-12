package ies.sequeros.dam.pmdm.gestionperifl.infraestructure.storage

interface TokenStorage {

    fun saveAccessToken(token: String)
    fun saveRefreshToken(token: String?)
    fun saveIdToken(token: String?)

    fun getAccessToken(): String?
    fun getRefreshToken(): String?
    fun getIdToken(): String?

    fun clear()
}