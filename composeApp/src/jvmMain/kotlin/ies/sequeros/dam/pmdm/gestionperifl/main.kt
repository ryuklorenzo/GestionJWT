package ies.sequeros.dam.pmdm.gestionperifl

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.ktor3.KtorNetworkFetcherFactory
import com.russhwolf.settings.PreferencesSettings
import com.russhwolf.settings.Settings
import ies.sequeros.dam.pmdm.gestionperifl.di.appModulo
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module
import java.util.prefs.Preferences

val DesktopPlatformModule = module {
    single<Settings> {
        val preferences = Preferences.userRoot().node("mi.app")
        PreferencesSettings(preferences)
    }
}
fun main() {
    startKoin {
        printLogger()
        modules(appModulo, DesktopPlatformModule)
    }





    application {
        setSingletonImageLoaderFactory { context ->
            ImageLoader.Builder(context)
                .components {
                    add(KtorNetworkFetcherFactory())
                }
                .build()
        }
        Window(
            onCloseRequest = ::exitApplication,
            title = "GestionJWT",
        ) {
            App()
        }
    }
}