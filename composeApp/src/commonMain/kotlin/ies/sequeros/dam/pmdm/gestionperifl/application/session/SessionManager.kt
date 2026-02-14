package ies.sequeros.dam.pmdm.gestionperifl.application.session

import ies.sequeros.dam.pmdm.gestionperifl.domain.model.AuthTokens
import ies.sequeros.dam.pmdm.gestionperifl.infraestructure.storage.SettingsTokenStorage
import ies.sequeros.dam.pmdm.gestionperifl.infraestructure.storage.TokenStorage

class SessionManager(private val tokenStorage: TokenStorage) {

    fun saveSession(responseTokens: AuthTokens) {
        tokenStorage.saveAllTokens(responseTokens)
    }

    fun saveSession(accessToken: String, idToken: String, refreshToken: String?) {
        val tokens = AuthTokens(accessToken, idToken, refreshToken)
        tokenStorage.saveAllTokens(tokens)
    }

    fun getAccessToken(): String? = tokenStorage.getAccessToken()

    fun getRefreshToken(): String? = tokenStorage.getRefreshToken()

    fun getIdToken(): String? = tokenStorage.getIdToken()

    fun isLoggedIn(): Boolean {
        return tokenStorage.getAccessToken() != null && tokenStorage.getIdToken() != null
    }

    fun clearSession(){
        tokenStorage.clear()
    }
}