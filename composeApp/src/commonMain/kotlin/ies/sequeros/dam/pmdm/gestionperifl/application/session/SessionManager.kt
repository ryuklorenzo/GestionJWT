package ies.sequeros.dam.pmdm.gestionperifl.application.session

import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import ies.sequeros.dam.pmdm.gestionperifl.application.usercase.LoginResponse

class SessionManager(private val settings: Settings) {

    companion object {
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
        private const val KEY_USER_ID = "id_token"
    }

    fun saveSession(response: LoginResponse) {
        settings.putString(KEY_ACCESS_TOKEN, response.access_token)
        settings.putString(KEY_USER_ID, response.id_token)
        response.refresh_token?.let {
            settings.putString(KEY_REFRESH_TOKEN, it)
        }
    }

    fun getAccessToken(): String? = settings.getStringOrNull(KEY_ACCESS_TOKEN) //[cite: 150]

    fun getRefreshToken(): String? = settings.getStringOrNull(KEY_REFRESH_TOKEN) //[cite: 150]

    fun isLoggedIn(): Boolean = getAccessToken() != null

    fun loguot() {
        settings.remove(KEY_ACCESS_TOKEN)
        settings.remove(KEY_REFRESH_TOKEN)
        settings.remove(KEY_USER_ID)
    }

}