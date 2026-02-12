package ies.sequeros.dam.pmdm.gestionperifl.infraestructure.storage

import com.russhwolf.settings.Settings
import ies.sequeros.dam.pmdm.gestionperifl.domain.model.AuthTokens

class SettingsTokenStorage(
    private val settings: Settings,
) : TokenStorage {

    companion object {
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_ID_TOKEN = "id_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
    }

    override fun saveAccessToken(token: String) {
        settings.putString(KEY_ACCESS_TOKEN, token)
    }
//guardamos los tokens de inicio de sesion
    fun saveAllTokens(tokens: AuthTokens) {
        settings.putString(KEY_ACCESS_TOKEN, tokens.accessToken)
        settings.putString(KEY_ID_TOKEN, tokens.idToken)
        tokens.refreshToken?.let {
            settings.putString(KEY_REFRESH_TOKEN, it)
        }
    }

    override fun saveRefreshToken(token: String?) {
        token?.let {
            settings.putString(KEY_REFRESH_TOKEN, it)
        }
    }

    override fun saveIdToken(token: String?) {
        token?.let {
            settings.putString(KEY_ID_TOKEN, it)
        }
    }

    override fun getAccessToken(): String? =
        settings.getStringOrNull(KEY_ACCESS_TOKEN)

    override fun getRefreshToken(): String? =
        settings.getStringOrNull(KEY_REFRESH_TOKEN)

    override fun getIdToken(): String? =
        settings.getStringOrNull(KEY_ID_TOKEN)

    override fun clear() {
        settings.remove(KEY_ACCESS_TOKEN)
        settings.remove(KEY_REFRESH_TOKEN)
        settings.remove(KEY_ID_TOKEN)
    }

    // metodo para obtener los tokens de inicio de sesion
    fun getFullTokens(): AuthTokens? {
        val access = getAccessToken()
        val id = getIdToken()
        val refresh = getRefreshToken()

        return if (access != null && id != null) {
            AuthTokens(access, id, refresh)
        } else {
            null
        }
    }

}